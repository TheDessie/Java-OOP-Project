package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application{

    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("loginView.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("RMIApp");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

}
