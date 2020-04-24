package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AlertBox {

    Stage stage;

    @FXML
    private Label lblOperation;

    @FXML
    private Button btnClose;

    // Иницииране на данните (предаване на данни межу различните сцени)
    public void initData(String operation) {

        lblOperation.setText(operation);
    }

    // Действие при натискане на бутона btnClose
    public void btnClosePushed(ActionEvent event) throws Exception{
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

}
