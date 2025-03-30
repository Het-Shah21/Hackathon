package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SettingBankAccountController {

    @FXML
    private ComboBox<String> selectBank;

    @FXML
    private TextField accountNumberField;

    @FXML
    private Button register;

    @FXML
    private PasswordField transPinField, authPinField;

    private Stage stage;

    public void initialize() {
        selectBank.getItems().add("ABC Bank");
        selectBank.getItems().add("State Bank of India");

        register.setOnAction(event -> {

            try {
                int check = Integer.parseInt(transPinField.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid PIN");
                alert.show();
                return;
            }

            try {
                int check = Integer.parseInt(authPinField.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid PIN");
                alert.show();
                return;
            }

            try {
                double check = Double.parseDouble(accountNumberField.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid account number");
                alert.show();
                return;
            }

            if (accountNumberField.getText().isEmpty() || selectBank.getSelectionModel().getSelectedItem() == null || transPinField.getText().isEmpty() || authPinField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter all the fields");
                alert.show();
            } else if (transPinField.getText().length() >= 6 || transPinField.getText().length() <= 4 || authPinField.getText().length() >= 6 || authPinField.getText().length() <= 4) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("PIN should be of length 4 to 6");
                alert.show();
            } else if (accountNumberField.getText().length() > 17 || accountNumberField.getText().length() < 11) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a account number of valid length");
                alert.show();
            } else {
                DataStore.getInstance().setBank(selectBank.getSelectionModel().getSelectedItem());
                DataStore.getInstance().setAuthPin(authPinField.getText());
                DataStore.getInstance().setTransPin(transPinField.getText());
                DataStore.getInstance().setAccountNumber(accountNumberField.getText());
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                DBUtils.changeScene(stage, "pin-confirm.fxml");
            }
        });
    }
}
