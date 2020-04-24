package server.func;

import com.thoughtworks.xstream.XStream;
import server.obj.Book;
import server.obj.Card;
import server.obj.Directory;
import server.obj.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenizableImpl extends UnicastRemoteObject implements Tokenizable {

    private User user = new User("", "", true, true);

    private Directory directory = null;

    private Book book = null;

    // в паметта се зареждат directory.xml(xml файл с всички имена на потребители, пароли и права);
    // и book.xml(xml файл с всички карти и сответните им токени).
    // Създават се обкти от тип Directory и Book
    protected TokenizableImpl() throws RemoteException {

        // обект от тип Directory
        String xml = "";
        File file = new File("directory.xml");
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                xml += myReader.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XStream xStream = new XStream();
        xStream.alias("directory", Directory.class);
        xStream.addImplicitCollection(Directory.class, "users");
        xStream.alias("user", User.class);
        xStream.aliasField("name", User.class, "name");
        xStream.aliasField("password", User.class, "password");
        xStream.aliasField("canRegisterToken", User.class, "canRegisterToken");
        xStream.aliasField("canGetCardNumber", User.class, "canGetCardNumber");
        directory = (Directory) xStream.fromXML(xml);

        // обект от тип Book
        xml = "";
        file = new File("book.xml");
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                xml += myReader.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        xStream = new XStream();
        xStream.alias("book", Book.class);
        xStream.addImplicitCollection(Book.class, "cards");
        xStream.alias("card", Card.class);
        xStream.aliasField("cardNumber", Card.class, "cardNumber");
        xStream.aliasField("cardToken", Card.class, "cardToken");
        book = (Book) xStream.fromXML(xml);

    }

    // regex валидация на номера на картата (валидиране чрез регулярен израз)
    private boolean isValidCardNumberRegex(String cardNumber) {
//        if (!cardNumber.matches("[3-6]{1}[0-9]{15}")) {
//            return false;
//        }
//        return true;

        String pattern = "[3-6]{1}[0-9]{15}";

        // Създава се Pattern object (модел)
        Pattern r = Pattern.compile(pattern);

        // Създава се Matcher object
        Matcher m = r.matcher(cardNumber);
        if (m.matches()) {
            return true;
        }
        else {
            return false;
        }
    }

    // regex валидация на токена на картата (валидиране чрез регулярен израз)
    private boolean isValidCardTokenRegex(String cardToken) {
//        if (!cardToken.matches("[0-2,7-9]{1}[0-9]{15}")) {
//            return false;
//        }
//        return true;

        String pattern = "[0-2,7-9]{1}[0-9]{15}";

        // Създава се Pattern object (модел)
        Pattern r = Pattern.compile(pattern);

        // Създава се Matcher object
        Matcher m = r.matcher(cardToken);
        if (m.matches()) {
            return true;
        }
        else {
            return false;
        }

    }

    // Валидация на номер на кредитна карта по алгоритъма на Luhn
    private boolean isValidCardNumberLuhn(int[] cardNumber) {
        int sum = 0;
        int tmp;

        for (int i = 1; i <= cardNumber.length; i++) {
            tmp = cardNumber[cardNumber.length - i];
            if (i % 2 == 0) {
                tmp *= 2;
                if (tmp > 9) {
                    sum += (tmp - 9);
                }
                else {
                    sum += tmp;
                }
            }
            else {
                sum += tmp;
            }
        }
        return sum % 10 == 0;
    }

    // Пресмятане на токен на карта
    private String calculateToken(int[] cardNumberIntArr) {

        int[] token = new int[cardNumberIntArr.length];
        int sum = 0;
        int tmpsum;
        int rand;
        Random r = new Random();

        for (int i = cardNumberIntArr.length - 1; i >= cardNumberIntArr.length - 4; i--) {
            token[i] = cardNumberIntArr[i];
            sum += token[i];
        }
        for (int i = 1; i < cardNumberIntArr.length - 4; i++) {
            rand = cardNumberIntArr[i];
            while(rand == cardNumberIntArr[i]) {
                rand = r.nextInt(10);
            }
            token[i] = rand;
            sum += token[i];
        }
        rand = 3;
        while (rand == 3 || rand == 4 || rand == 5 || rand == 6) {
            tmpsum = sum;
            rand = r.nextInt(10);
            tmpsum += rand;
            if (tmpsum % 10 == 0) {
                rand = 3;
            }
        }
        token[0] = rand;

        String cardToken = "";
        for (int i = 0; i < token.length; i++) {
            cardToken += token[i];
        }
        return cardToken;
    }

    @Override
    // Създава и връща токен по даден номер на карта
    public String getCardToken(String cardNumber) throws RemoteException {

        if(!isValidCardNumberRegex(cardNumber)) {
            return null; // "Invalid card number!"
        }

        int[] cardNumberIntArr = new int[cardNumber.length()];
        for (int i = 0; i < cardNumber.length(); i++) {
            cardNumberIntArr[i] = Character.digit(cardNumber.charAt(i), 10);
        }

        if (!isValidCardNumberLuhn(cardNumberIntArr)) {
            return null; // "Invalid card number!"
        }

        String cardToken = "";
        Card card = null;
        boolean flag = true;
        while (flag) {
            cardToken = calculateToken(cardNumberIntArr);
            card = book.getCardByToken(cardToken);
            if (card == null) {
                flag = false;
            }
        }

        book.addNewToken(cardNumber, cardToken);

        // Превтъщане на обект от тип Book в xml
        XStream xStream = new XStream();
        xStream.alias("book", Book.class);
        xStream.addImplicitCollection(Book.class, "cards");
        xStream.alias("card", Card.class);
        xStream.aliasField("cardNumber", Card.class, "cardNumber");
        xStream.aliasField("cardToken", Card.class, "cardToken");

        String xml = xStream.toXML(book);
        // Допълване на xml файла с новосъздадения запис
        File file = new File("book.xml");
        try {
            FileWriter myWriter = new FileWriter("book.xml");
            myWriter.write(xml);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return cardToken;
    }

    @Override
    // Връща номер на карта по даден токен
    public String getCardNumber(String cardToken) throws RemoteException {

        if (!isValidCardTokenRegex(cardToken)) {
            return null; // "Inalid card token!"
        }

        Card card = book.getCardByToken(cardToken);
        if (card != null) {
            return card.getCardNumber();
        }

        return null; //"This card token doesn't exist!"
    }

    @Override
    // Връща дали даден потребител има право да регистрира токени или да извлича номер на карта (или и двете)
    public boolean isAuthorised(String accessType) throws RemoteException {
        if (accessType.equals("canRegisterToken")) {
            return user.isCanRegisterToken();
        }
        else if (accessType.equals("canGetCardNumber")) {
            return user.isCanGetCardNumber();
        }
        else {
            return false;
        }
    }

    @Override
    // Регистриране на потребител
    public boolean registerUser(String name, String password, boolean canRegisterToken, boolean canGetCardNumber) throws RemoteException {
        User user = directory.getUserByName(name);
        if (user == null) {
            directory.addUser(name, password, canRegisterToken, canGetCardNumber);

            // Превтъщане на обект от тип Directory в xml
            XStream xStream = new XStream();
            xStream.alias("directory", Directory.class);
            xStream.addImplicitCollection(Directory.class, "users");
            xStream.alias("user", User.class);
            xStream.aliasField("name", User.class, "name");
            xStream.aliasField("password", User.class, "password");
            xStream.aliasField("canRegisterToken", User.class, "canRegisterToken");
            xStream.aliasField("canGetCardNumber", User.class, "canGetCardNumber");

            String xml = xStream.toXML(directory);

            // Допълване на xml файла с новосъздадения запис
            File file = new File("directory.xml");
            try {
                FileWriter myWriter = new FileWriter("directory.xml");
                myWriter.write(xml);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
        else {
            return false; // "This username already exists!"
        }
    }

    @Override
    // Вход в системата
    public boolean login(String name, String password) throws RemoteException {

        User userTmp = directory.getUserByName(name);
        if (userTmp == null) {
            user = null;
            return false; // "Wrong username or password!"
        }
        else {
            user = userTmp;
            return userTmp.getPassword().equals(password);
        }
    }

    @Override
    // Връща списък от сортираните карти и токени по номер на карта или по токен
    public List<String> report(String orderBy) throws RemoteException {

        List<String> lines = new ArrayList<String>();
        String currentCard = "";
        int size = book.getCards().size();

        if (orderBy.equals("cardNumber")) {
            Collections.sort(book.getCards(), new Comparator<Card>() {
                public int compare(Card c1, Card c2) {
                    int res = c1.getCardNumber().compareTo(c2.getCardNumber());
                    if (res == 0) {
                        return c1.getCardToken().compareTo(c2.getCardToken());
                    }
                    else {
                        return res;
                    }
                }
            });

            for (int i = 0; i < size; i++) {
                currentCard = book.getCards().get(i).getCardNumber();
                currentCard += " ";
                currentCard += book.getCards().get(i).getCardToken();
                lines.add(currentCard);
            }

        }
        else { // orderBy.equals("cardToken")
            Collections.sort(book.getCards(), new Comparator<Card>() {
                public int compare(Card c1, Card c2) {
                    return c1.getCardToken().compareTo(c2.getCardToken());
                }
            });

            for (int i = 0; i < size; i++) {
                currentCard = book.getCards().get(i).getCardToken();
                currentCard += " ";
                currentCard += book.getCards().get(i).getCardNumber();
                lines.add(currentCard);
            }
        }

        return lines;


    }

}




























