package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class PinConfirmController {

    @FXML
    private PasswordField authPinConfirmField;

    @FXML
    private PasswordField transPinConfirmField;

    @FXML
    private Button proceed;

    private Stage stage;

    public void initialize() {
        proceed.setOnAction(e -> {
            if (!authPinConfirmField.getText().equals(DataStore.getInstance().getAuthPin())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid Auth PIN");
                alert.show();
            } else if (!transPinConfirmField.getText().equals(DataStore.getInstance().getTransPin())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Invalid transaction PIN");
                alert.show();
            } else {
                String upi = UpiIdGenerator.generateUpiId(DataStore.getInstance().getPhoneNumber(), DataStore.getInstance().getBank());
                DBUtils.registerUser(DataStore.getInstance().getName(), DataStore.getInstance().getUsername(), DataStore.getInstance().getPassword(), DataStore.getInstance().getEmail(), DataStore.getInstance().getPhoneNumber(), DataStore.getInstance().getBank(), DataStore.getInstance().getAccountNumber(), upi, DataStore.getInstance().getTransPin(), DataStore.getInstance().getAuthPin());
                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                DBUtils.changeScene(stage, "main.fxml");
            }
        });
    }
}
