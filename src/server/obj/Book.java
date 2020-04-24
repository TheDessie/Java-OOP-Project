package server.obj;

import java.util.List;

public class Book {

    private List<Card> cards;

    public Book() {
    }

    public Book(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    // Връща обект Card по даден номер на карта
    public Card getCardByNumber(String cardNumber) {

        int countOfCards = cards.size();

        for (int i = 0; i < countOfCards; i++) {
            if (cards.get(i).getCardNumber().equals(cardNumber)) {
                return cards.get(i);
            }
        }
        return null; // "This card number does not exist!"
    }

    // Връща обект Card по даден токен
    public Card getCardByToken(String cardToken) {

        int countOfCards = cards.size();

        for (int i = 0; i < countOfCards; i++) {
            if (cards.get(i).getCardToken().equals(cardToken)) {
                return cards.get(i);
            }
        }
        return null;
    }

    // Добавя карта в Book
    public boolean addNewToken(String cardNumber, String cardToken) {

        Card card = new Card(cardNumber, cardToken);
        cards.add(card);

        return true;
    }
}
