package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginScreen {

    @FXML
    private Button loginButton;

    @FXML
    private Label loginOrRegister;

    @FXML
    private Label login;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label register;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private Label nameLabel, email;

    @FXML
    private Label phoneNumber;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Button registerButton;

    private Stage stage;

    public void initialize() {

        email.setVisible(false);
        phoneNumber.setVisible(false);
        phoneNumberTextField.setVisible(false);
        emailField.setVisible(false);
        nameLabel.setVisible(false);
        nameTextField.setVisible(false);
        registerButton.setVisible(false);
        login.setVisible(false);

        loginButton.setOnAction(e -> {

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter all fields");
                alert.show();
                return;
            }

            if (DBUtils.isLoggedIn(username, password)) {
                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                DBUtils.changeScene(stage, "main.fxml");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Incorrect username or password");
                alert.show();
            }
        });

        register.setOnMouseClicked(e -> {
            nameLabel.setVisible(true);
            nameTextField.setVisible(true);
            registerButton.setVisible(true);
            emailField.setVisible(true);
            phoneNumberTextField.setVisible(true);
            loginButton.setVisible(false);
            email.setVisible(true);
            phoneNumber.setVisible(true);
            register.setVisible(false);
            login.setVisible(true);

            loginOrRegister.setText("Register");
        });

        login.setOnMouseClicked(e -> {
            nameLabel.setVisible(false);
            nameTextField.setVisible(false);
            registerButton.setVisible(false);
            emailField.setVisible(false);
            phoneNumberTextField.setVisible(false);
            loginButton.setVisible(true);
            phoneNumber.setVisible(false);
            email.setVisible(false);
            register.setVisible(true);
            login.setVisible(false);

            loginOrRegister.setText("Login");
        });

        registerButton.setOnAction(e -> {

            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || emailField.getText().isEmpty() || phoneNumberTextField.getText().isEmpty() || nameTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter all fields");
                alert.show();
                return;
            }

            String nameString = nameTextField.getText();
            String emailString = emailField.getText();
            String phoneNumberString = phoneNumberTextField.getText();
            String passwordString = passwordField.getText();
            String usernameString = usernameField.getText();

            DataStore.getInstance().setUsername(usernameString);
            DataStore.getInstance().setEmail(emailString);
            DataStore.getInstance().setName(nameString);
            DataStore.getInstance().setPassword(passwordString);
            DataStore.getInstance().setPhoneNumber(phoneNumberString);

            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            DBUtils.changeScene(stage, "setting-bank-account.fxml");
        });
    }
}