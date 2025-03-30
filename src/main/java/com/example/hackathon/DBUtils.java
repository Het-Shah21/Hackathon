package com.example.hackathon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DBUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/nu_hackathon";
    private static final String DB_USER = "pratham";
    private static final String DB_PASSWORD = "hapy1234";

    public static void changeScene(Stage stage, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoggedIn(String username, String password) {
        Connection connection = null;
        PreparedStatement psCheckUser = null;

        boolean isUserExist = false;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psCheckUser = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");

            psCheckUser.setString(1, username);
            psCheckUser.setString(2, password);

            isUserExist = psCheckUser.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psCheckUser != null) {
                try {
                    psCheckUser.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } return isUserExist;
    }

    public static void registerUser(String name, String username, String password, String email, String phoneNumber, String bank, String accountNumber, String upi, String transPin, String pin) {
        Connection connection = null;
        PreparedStatement psCheckUser = null;
        PreparedStatement psInsertUser = null;
        boolean isUserExist;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psCheckUser = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUser.setString(1, username);
            isUserExist = psCheckUser.executeQuery().next();

            if (isUserExist) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("User already exists");
                alert.show();
            } else {
                psInsertUser = connection.prepareStatement("INSERT INTO users (name, username, password, email, phone_no, primary_bank, account_number, upi_id, transaction_pin, pin, registration_date, last_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psInsertUser.setString(1, name);
                psInsertUser.setString(2, username);
                psInsertUser.setString(3, password);
                psInsertUser.setString(4, email);
                psInsertUser.setString(5, phoneNumber);
                psInsertUser.setString(6, bank);
                psInsertUser.setString(7, accountNumber);
                psInsertUser.setString(8, upi);
                psInsertUser.setString(9, transPin);
                psInsertUser.setString(10, pin);
                psInsertUser.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
                psInsertUser.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
                psInsertUser.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
