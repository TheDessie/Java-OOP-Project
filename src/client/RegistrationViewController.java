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

public class RegistrationViewController {

    // for the back button
    private boolean canRegisterToken = false;
    private boolean canGetCardNumber = false;
    private String name = "";
    boolean isAdmin = false;
    // --------------------------------------

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblAboutUsername1;

    @FXML
    private Label lblAboutUsername2;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtConfirmPassword;

    @FXML
    private Label lblAboutPassword1;

    @FXML
    private Label lblAboutPassword2;

    @FXML
    private Label lblAboutPassword3;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnRegister;

    @FXML
    private TextField txtRightToRegisterToken;

    @FXML
    private TextField txtRightToGetCardNumber;

    // Иницииране на данните (предаване на данни межу различните сцени)
    public void initData(String name, boolean isAdmin, boolean canRegisterToken, boolean canGetCardNumber) {

        this.name = name;
        this.isAdmin = isAdmin;
        this.canRegisterToken = canRegisterToken;
        this.canGetCardNumber = canGetCardNumber;
    }

    // Действие при натискане на бутона btnRegister
    public void btnRegisterPushed(ActionEvent event) throws IOException, NotBoundException {

        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            lblAboutPassword1.setVisible(true);
            lblAboutPassword2.setVisible(true);
            lblAboutPassword3.setVisible(true);
        }
        else {

            String userName = txtUsername.getText();
            String password = txtPassword.getText();
            boolean rightToRegisterToken = false;
            boolean rightToGetCardNumber = false;

            if (txtRightToRegisterToken.getText().equals("y")) {
                rightToRegisterToken = true;
            }

            if (txtRightToGetCardNumber.getText().equals("y")) {
                rightToGetCardNumber = true;
            }

            // Създаване на обект от тип Tokenizable
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Tokenizable object = null;
            object = (Tokenizable) registry.lookup("CardToken");

            if (object.registerUser(userName, password, rightToRegisterToken, rightToGetCardNumber)) {

                // Зареждане на сцената от successfulOperationView.fxml
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("successfulOperationView.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);

                // Access the controller and call a method
                SuccessfulOperationViewController controller = loader.getController();
                controller.initData("registration", name, isAdmin, canRegisterToken, canGetCardNumber);

                // This line gets the Stage information
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                window.setScene(scene);
                window.show();
            }
            else {
                lblAboutUsername1.setVisible(true);
                lblAboutUsername2.setVisible(true);
            }

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
