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
import server.obj.User;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginViewController {

    public User user = new User();
    public boolean isLogged = false;
    public boolean isAdmin = false;
    public String operation = "";
    public boolean canRegisterToken = false;
    public boolean canGetCardNumber = false;

    @FXML
    private Label lblIncorrectData;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    // Действие при натискане на бутона btnLogin
    public void loginButtonPushed(ActionEvent event) throws IOException, RemoteException, NotBoundException, AccessException{

        // Създаване на обект от тип Tokenizable
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        Tokenizable object = null;
        object = (Tokenizable) registry.lookup("CardToken");

        // Проверка за валидни данни
        String name = txtName.getText();
        String password = txtPassword.getText();

        if (object.login(name, password)) {

            isLogged = true;

            if (name.equals("admin")) {
                isAdmin = true;
            }

            canRegisterToken = object.isAuthorised("canRegisterToken");
            canGetCardNumber = object.isAuthorised("canGetCardNumber");

            // Започва подготовката за смяна за сцената - преход към welcomeView.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("welcomeView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            // Access the controller and call a method
            WelcomeViewController controller = loader.getController();
            controller.initData(name, isAdmin, canRegisterToken, canGetCardNumber);

            // This line gets the Stage information
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(scene);
            window.show();
        }
        else {

            lblIncorrectData.setVisible(true);
        }

    }


}
