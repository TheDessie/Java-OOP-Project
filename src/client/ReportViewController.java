package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.func.Tokenizable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReportViewController {

    String fileName = "";
    String pathFile = "";

    // for the Home button
    private boolean canRegisterToken = false;
    private boolean canGetCardNumber = false;
    private String name = "";
    boolean isAdmin = false;
    // --------------------------------------

    @FXML
    private TextField txtFileName;

    @FXML
    private Button btnOrderByCardNumber;

    @FXML
    private Button btnOrderByToken;

    // Иницииране на данните (предаване на данни межу различните сцени)
    public void initData(String name, boolean isAdmin, boolean canRegisterToken, boolean canGetCardNumber) {

        this.name = name;
        this.isAdmin = isAdmin;
        this.canRegisterToken = canRegisterToken;
        this.canGetCardNumber = canGetCardNumber;
    }

    // Създава текстов файл, съдържащ таблица с всички банкови карти и съответните им токени
    public void createReport(ActionEvent event, String order) throws IOException, NotBoundException {

        fileName = txtFileName.getText();
        pathFile = "C:\\Users\\aless\\Desktop\\" + fileName;

        // Създаване на обект от тип Tokenizable
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        Tokenizable object = null;
        object = (Tokenizable) registry.lookup("CardToken");

        List<String> lines = object.report(order);
        // Създаване на текстов файл
        Path file = Paths.get(pathFile);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // При успех зарежда сцената от successfulOperationView.fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("successfulOperationView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Access the controller and call a method
        SuccessfulOperationViewController controller = loader.getController();
        controller.initData("report", name, isAdmin, canRegisterToken, canGetCardNumber);

        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    // Действие при натискане на бутона btnOrderByCardNumber
    public void btnOrderByCardNumberPushed(ActionEvent event) throws IOException, NotBoundException {

        createReport(event, "cardNumber");
    }

    // Действие при натискане на бутона btnOrderByToken
    public void btnOrderByTokenPushed(ActionEvent event) throws IOException, NotBoundException {

        createReport(event, "cardToken");
    }



}
