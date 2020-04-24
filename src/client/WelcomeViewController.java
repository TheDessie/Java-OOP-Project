package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class WelcomeViewController {

    private boolean canRegisterToken = false;
    private boolean canGetCardNumber = false;
    private String operation = "";

    private String name = "";
    boolean isAdmin = false;

    @FXML
    private Label lblNameOfCurrentUser;

    @FXML
    private Button btnTokenize;

    @FXML
    private Button btnGetCardNumber;

    @FXML
    private Button btnRegisterUser;

    @FXML
    private Button btnReport;

    // Иницииране на данните (предаване на данни межу различните сцени)
    public void initData(String name, boolean isAdmin, boolean canRegisterToken, boolean canGetCardNumber) {

        this.canRegisterToken = canRegisterToken;
        this.canGetCardNumber = canGetCardNumber;

        this.name = name;
        this.isAdmin = isAdmin;

        lblNameOfCurrentUser.setText(name);

        if (isAdmin) {
            btnRegisterUser.setVisible(true);
            btnReport.setVisible(true);
        }
    }

    // Зарежда сцената на operationView.fxml
    public void goToOperationView(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("operationView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Access the controller and call a method
        OperationViewController controller = loader.getController();

        controller.initData(operation, name, isAdmin, canRegisterToken, canGetCardNumber);

        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    // Зарежда сцената на alertBox.fxml
    public void goToAlertBox(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("alertBox.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Alert Box");
        Scene scene = new Scene(root);

        // Access the controller and call a method
        AlertBox controller = loader.getController();

        if (operation.equals("token")) {
            controller.initData("tokenize a bank card");
        }
        else { // operation.equals("card number")
            controller.initData("get a card number");
        }

        stage.setScene(scene);
        stage.showAndWait();
    }

    // Действие при натискане на бутона btnTokenize
    public void btnTokenizePushed(ActionEvent event) throws IOException, RemoteException, NotBoundException, AccessException {

        operation = "token";

        if (canRegisterToken) {
            goToOperationView(event);
        }
        else {
            goToAlertBox(event);
        }

    }

    // Действие при натискане на бутона btnGetCardNumber
    public void btnGetCardNumberPushed(ActionEvent event) throws IOException, RemoteException, NotBoundException, AccessException {

        operation = "card number";

        if (canGetCardNumber) {
            goToOperationView(event);
        }
        else {
            goToAlertBox(event);
        }
    }

    // Действие при натискане на бутона btnRegisterUser
    public void btnRegisterUserPushed(ActionEvent event) throws IOException, RemoteException, NotBoundException, AccessException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("registrationView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Access the controller and call a method
        RegistrationViewController controller = loader.getController();
        controller.initData(name, isAdmin, canRegisterToken, canGetCardNumber);

        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    // Действие при натискане на бутона btnReport
    public void btnReportPushed(ActionEvent event) throws IOException, RemoteException, NotBoundException, AccessException {

        // Зареждане на сцената от reportView.fxml
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("reportView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // Access the controller and call a method
        ReportViewController controller = loader.getController();
        controller.initData(name, isAdmin, canRegisterToken, canGetCardNumber);

        // This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

}
