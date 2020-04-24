package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.func.Tokenizable;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class OperationViewController {

    private String operation = "";
    private String dataToEnterTxt = "";
    String result = "";

    // for the back button
    private boolean canRegisterToken = false;
    private boolean canGetCardNumber = false;
    private String name = "";
    boolean isAdmin = false;
    // --------------------------------------

    @FXML
    private Label lblOperation;

    @FXML
    private TextField txtCardData;

    @FXML
    private Label lblYouEnteredAnInvalid;

    @FXML
    private Label lbl2Operation;

    @FXML
    private Label lbl3;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnBack;

    // Иницииране на данните (предаване на данни межу различните сцени)
    public void initData(String operation, String name, boolean isAdmin, boolean canRegisterToken, boolean canGetCardNumber) {
        this.operation = operation;
        if(operation.equals("token")) {
            lblOperation.setText("card number");
        }
        else { // operation.equals("card number")
            lblOperation.setText("token");
        }

        this.name = name;
        this.isAdmin = isAdmin;
        this.canRegisterToken = canRegisterToken;
        this.canGetCardNumber = canGetCardNumber;
    }

    // Действие при натискане на бутона btnSubmit
    public void btnSubmitPushed(ActionEvent event) throws IOException, NotBoundException {

        // Създаване на обект от тип Tokenizable
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        Tokenizable object = null;
        object = (Tokenizable) registry.lookup("CardToken");

        String cardData = txtCardData.getText();

        if (operation.equals("token")) {
            dataToEnterTxt = "card number";
            result = object.getCardToken(cardData);
        }
        else { // operation.equals("card number")
            dataToEnterTxt = "token";
            result = object.getCardNumber(cardData);
        }

        if (result != null) {

            // Зареждане на сцената от resultView.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("resultView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Access the controller and call a method
            ResultViewController controller = loader.getController();
            controller.initData(operation, result, name, isAdmin, canRegisterToken, canGetCardNumber);

            // This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        }
        else {
            lblYouEnteredAnInvalid.setVisible(true);
            lbl2Operation.setText(dataToEnterTxt);
            lbl2Operation.setVisible(true);
            lbl3.setVisible(true);
        }
    }

    // Действие при натискане на бутона btnBack
    public void btnBackPushed(ActionEvent event) throws IOException {

        // Зареждане на сцената от welcomeView.fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("welcomeView.fxml"));
        Parent loginViewParent = loader.load();

        Scene loginViewScene = new Scene(loginViewParent);

        // Access the controller and call a method
        WelcomeViewController controller = loader.getController();
        controller.initData(name, isAdmin, canRegisterToken, canGetCardNumber);

        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(loginViewScene);
        window.show();
    }

}
