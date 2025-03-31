package com.example.hackathon;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean isUserExists(String username) {
        Connection connection = null;
        PreparedStatement psCheckUser = null;

        boolean isUserExist = false;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psCheckUser = connection.prepareStatement("SELECT * FROM users WHERE username = ?");

            psCheckUser.setString(1, username);

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

    public static void updateRecentLogin(String username) {
        Connection connection = null;
        PreparedStatement psUpdateLogin = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psUpdateLogin = connection.prepareStatement("UPDATE users SET last_login = NOW() WHERE username = ?");
            psUpdateLogin.setString(1, username);
            psUpdateLogin.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psUpdateLogin != null) {
                try {
                    psUpdateLogin.close();
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
        }
    }

    public static void addTransaction(String username, String transactionId, BigDecimal amount, String location, String currency) {
        Connection connection = null;
        PreparedStatement psAddTransaction = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psAddTransaction = connection.prepareStatement("INSERT INTO transactions (username, transaction_id, amount, transaction_time, location, currency) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)");
            psAddTransaction.setString(1, username);
            psAddTransaction.setString(2, transactionId);
            psAddTransaction.setBigDecimal(3, amount);
            psAddTransaction.setString(4, location);
            psAddTransaction.setString(5, currency);
            psAddTransaction.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psAddTransaction != null) {
                try {
                    psAddTransaction.close();
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
        }
    }

    public static List<Transactions> showTransactions(String username) {
        List<Transactions> transactions = new ArrayList<>();

        Connection connection = null;
        PreparedStatement psShowTransactions = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psShowTransactions = connection.prepareStatement("SELECT * FROM transactions WHERE username = ? ORDER BY transaction_time DESC LIMIT 20");
            psShowTransactions.setString(1, username);
            rs = psShowTransactions.executeQuery();

            while (rs.next()) {
                transactions.add(new Transactions(rs.getString("username"),
                        rs.getString("transaction_id"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_time"),
                        rs.getString("location"),
                        rs.getString("currency")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psShowTransactions != null) {
                try {
                    psShowTransactions.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (rs != null) {
                try {
                    rs.close();
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
        } return transactions;
    }

    public static List<Transactions> showTransactionHistory(String username) {
        List<Transactions> transactions = new ArrayList<>();

        Connection connection = null;
        PreparedStatement psShowTransactions = null;
        ResultSet rs = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psShowTransactions = connection.prepareStatement("SELECT * FROM transactions WHERE username = ?");
            psShowTransactions.setString(1, username);
            rs = psShowTransactions.executeQuery();

            while (rs.next()) {
                transactions.add(new Transactions(rs.getString("username"),
                        rs.getString("transaction_id"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_time"),
                        rs.getString("location"),
                        rs.getString("currency")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psShowTransactions != null) {
                try {
                    psShowTransactions.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (rs != null) {
                try {
                    rs.close();
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
        } return transactions;
    }

    public static String getTransactionPin(String username) {
        Connection connection = null;
        PreparedStatement psGetTransactionPin = null;
        ResultSet rs = null;
        int transactionPin = 0;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psGetTransactionPin = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psGetTransactionPin.setString(1, username);
            rs = psGetTransactionPin.executeQuery();

            while (rs.next()) {
                transactionPin = rs.getInt("transaction_pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psGetTransactionPin != null) {
                try {
                    psGetTransactionPin.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (rs != null) {
                try {
                    rs.close();
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
        } return String.valueOf(transactionPin);
    }

    public static String getPhoneNumber(String username) {
        Connection connection = null;
        PreparedStatement psGetPhoneNumber = null;
        ResultSet rs = null;
        String phoneNumber = "";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psGetPhoneNumber = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psGetPhoneNumber.setString(1, username);
            rs = psGetPhoneNumber.executeQuery();

            while (rs.next()) {
                phoneNumber = rs.getString("phone_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psGetPhoneNumber != null) {
                try {
                    psGetPhoneNumber.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (rs != null) {
                try {
                    rs.close();
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
        } return phoneNumber;
    }

    public static String getEmail(String username) {
        Connection connection = null;
        PreparedStatement psGetEmail = null;
        ResultSet rs = null;
        String email = "";

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psGetEmail = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psGetEmail.setString(1, username);
            rs = psGetEmail.executeQuery();

            while (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (psGetEmail != null) {
                try {
                    psGetEmail.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } if (rs != null) {
                try {
                    rs.close();
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
        } return email;
    }
}
