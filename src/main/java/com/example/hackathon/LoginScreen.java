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

        Tooltip emailTooltip = new Tooltip("Invalid email format!");
        emailTooltip.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidEmail(newValue)) {
                emailField.setStyle("-fx-border-color: green;");
                Tooltip.uninstall(emailField, emailTooltip);
            } else {
                emailField.setStyle("-fx-border-color: red;");
                Tooltip.install(emailField, emailTooltip);
            }
        });

        Tooltip phoneTooltip = new Tooltip("Phone number must be exactly 10 digits!");
        phoneTooltip.setStyle("-fx-background-color: red; -fx-text-fill: white;");

        phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValidPhoneNumber(newValue)) {
                phoneNumberTextField.setStyle("-fx-border-color: green;");
                Tooltip.uninstall(phoneNumberTextField, phoneTooltip);
            } else {
                phoneNumberTextField.setStyle("-fx-border-color: red;");
                Tooltip.install(phoneNumberTextField, phoneTooltip);
            }
        });

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
                DBUtils.updateRecentLogin(username);
                DataStore.getInstance().setUsername(username);
                DataStore.getInstance().setTransPin(DBUtils.getTransactionPin(username));
                DataStore.getInstance().setEmail(DBUtils.getEmail(username));
                DataStore.getInstance().setPhoneNumber(DBUtils.getPhoneNumber(username));
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
            } else if (DBUtils.isUserExists(usernameField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("User already exists");
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

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}"); // Ensures exactly 10 digits
    }
}