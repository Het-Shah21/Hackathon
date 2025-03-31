package com.example.hackathon;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionController implements Initializable {

    // Main components
    @FXML private BorderPane mainContainer;
    @FXML private RadioButton sendPaymentRadio;
    @FXML private RadioButton receivePaymentRadio;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> currencyComboBox;
    @FXML private Label counterpartyLabel;
    @FXML private TextField nameField;
    @FXML private TextField accountField;
    @FXML private TextField bankField;
    @FXML private TextField countryField;
    @FXML private TextField tranPinField;
    @FXML private TextField smsOtpField;
    @FXML private TextField emailOtpField;
    @FXML private Label resendSms;
    @FXML private Label resendEmail;
    @FXML private TextArea purposeField;
    @FXML private VBox tranPin, emailOtp;

    // Risk assessment components
    @FXML private VBox riskAssessmentContainer;
    @FXML private Rectangle riskIndicator;
    @FXML private Label riskLevelLabel;
    @FXML private Label riskMessageLabel;

    // Buttons
    @FXML private Button assessRiskButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;

    private String risk;
    private String otpSms;
    private String otpEmail;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/chat/completions";
    private static final String API_KEY = "sk-4072ed376d924697bd1edd82ec5ea149";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tranPin.setVisible(false);
        emailOtp.setVisible(false);

        // Setup currency dropdown
        currencyComboBox.setItems(FXCollections.observableArrayList(
                "INR - Indian Rupee", "USD - US Dollar", "EUR - Euro"
        ));
        currencyComboBox.getSelectionModel().selectFirst();

        sendPaymentRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) counterpartyLabel.setText("Recipient Information");
        });

        receivePaymentRadio.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) counterpartyLabel.setText("Sender Information");
        });

        // Numeric validation for amount
        amountField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                amountField.setText(oldVal);
            }
        });

        cancelButton.setOnAction(event -> clearForm());
    }

    /**
     * Handles the risk assessment process when the "Assess Risk" button is clicked.
     */
    @FXML
    private void handleRiskAssessment() {
        if (!validateForm()) {
            showAlert("Form Validation Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
            return;
        }

        assessRiskButton.setDisable(true);
        assessRiskButton.setText("Processing...");

        CompletableFuture.supplyAsync(this::sendToDeepSeek)
                .thenAccept(result -> javafx.application.Platform.runLater(() -> {
                    displayRiskAssessmentResults(result);
                    assessRiskButton.setDisable(false);
                    assessRiskButton.setText("Reassess Risk");

                    confirmButton.setDisable(result.riskLevel.equals("CRITICAL"));
                }));
    }

    /**
     * Sends transaction data to DeepSeek API for risk assessment.
     */
    private RiskAssessmentResult sendToDeepSeek() {

        try {
            // Prepare JSON request payload
            JSONObject requestPayload = new JSONObject();
            requestPayload.put("model", "deepseek-chat");
            requestPayload.put("messages", new JSONObject[] {
                    new JSONObject().put("role", "system").put("content", "You are a financial fraud detection AI. You will be given data of five recent transactions of the user and the current one being processed. You have to assess risk levels LOW, MEDIUM, HIGH, CRITICAL. Default risk level is low, medium when transaction amount unusually high or unusual location, or transaction during night-time. High when unusually high amount from an unusual location or unusually high amount at night. Critical when transaction from different locations in short time period."),
                    new JSONObject().put("role", "user").put("content", getTransactionDetails())
            });
            requestPayload.put("temperature", 0.3);
            requestPayload.put("max_tokens", 100);

            // Send HTTP request
            String response = sendPostRequest(requestPayload.toString());
            JSONObject jsonResponse = new JSONObject(response);

            String riskLevel = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").toUpperCase();
            return new RiskAssessmentResult(riskLevel, "AI analysis completed.");
        } catch (Exception e) {
            e.printStackTrace();
            return new RiskAssessmentResult("UNKNOWN", "Error in risk assessment.");
        }
    }

    /**
     * Prepares transaction details as input for DeepSeek AI.
     */
    private String getTransactionDetails() {
        List<Transactions> transactions = DBUtils.showTransactions(DataStore.getInstance().getUsername());

        StringBuilder transactionHistory = new StringBuilder();
        for (Transactions t : transactions) {
            transactionHistory.append("\n- ")
                    .append("Amount: ").append(t.getAmount()).append(" ").append(t.getCurrency())
                    .append(", Location: ").append(t.getLocation())
                    .append(", Time: ").append(t.getTransactionTime());
        }

        return "Assess the risk of this transaction:\n" +
                "Amount: " + amountField.getText() + " " + currencyComboBox.getValue() + "\n" +
                "Sender: " + DataStore.getInstance().getUsername() + "\n" +
                "Location: " + getSenderCity() + "\n" +
                "Transaction time: " + Timestamp.from(Instant.now()) + "\n" +
                "Previous Transactions (Last " + transactions.size() + "): " + transactionHistory + "\n" +
                "Risk levels: LOW, MEDIUM, HIGH, CRITICAL. Reply with only the risk level.";
    }

    /**
     * Sends a POST request to DeepSeek API.
     */
    private String sendPostRequest(String jsonInput) throws Exception {
        URL url = new URL(DEEPSEEK_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    /**
     * Displays the risk assessment results in the UI.
     */
    private void displayRiskAssessmentResults(RiskAssessmentResult result) {
        String riskColor = switch (result.riskLevel) {
            case "LOW" -> "green";
            case "MEDIUM" -> "yellow";
            case "HIGH" -> "orange";
            case "CRITICAL" -> "red";
            default -> "gray";
        };

        riskIndicator.getStyleClass().removeAll("green", "yellow", "orange", "red", "gray");
        riskIndicator.getStyleClass().add(riskColor);

        riskLevelLabel.setText(result.riskLevel);
        riskMessageLabel.setText(result.message);

        if (!riskAssessmentContainer.isVisible()) {
            switch (result.riskLevel) {
                case "LOW" -> {
                    risk = "LOW";
                    tranPin.setVisible(true);
                    tranPin.setManaged(true);
                }
                case "MEDIUM" -> {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    risk = "MEDIUM";
                    tranPin.setVisible(true);
                    tranPin.setManaged(true);
//                    smsOtp.setVisible(true);
//                    smsOtp.setManaged(true);
//                    otpSms = SMSOTPService.generateOTP();
//                    SMSOTPService.sendOTP(DataStore.getInstance().getPhoneNumber(), otpSms);
//                    executor.submit(() -> {
//                        try {
//                            SMSOTPService smsotpService = new SMSOTPService(
//                                    "AC5d39be1771ee1521840fc64595927673",
//                                    "5c8c8b1cf05de4f0c28adb131731bc97",
//                                    "+91 6355 500 475"
//                            );
//
//                            smsotpService.sendOTP("+91" + DataStore.getInstance().getPhoneNumber(), otpSms);
//
//                            Platform.runLater(() -> {
//                                System.out.println("OTP Sent Successfully!");
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Platform.runLater(() -> System.out.println("Failed to send OTP!"));
//                        }
//                    });
                    emailOtp.setVisible(true);
                    emailOtp.setManaged(true);
                    otpEmail = String.format("%06d", new Random().nextInt(1000000));
                    executorService.execute(() -> EmailOTPService.sendOTP(DataStore.getInstance().getEmail(), otpEmail));
                }

                case "HIGH" -> {
                    risk = "HIGH";
                    tranPin.setVisible(true);
                    tranPin.setManaged(true);
//                    smsOtp.setVisible(true);
//                    smsOtp.setManaged(true);
                    emailOtp.setVisible(true);
                    emailOtp.setManaged(true);

                    otpEmail = String.format("%06d", new Random().nextInt(1000000));
                    executorService.execute(() -> EmailOTPService.sendOTP(DataStore.getInstance().getEmail(), otpEmail));
                }
                case "CRITICAL" -> {
                    risk = "CRITICAL";
//                    tranPin.setVisible(true);
//                    tranPin.setManaged(true);
////                    smsOtp.setVisible(true);
////                    smsOtp.setManaged(true);
//                    emailOtp.setVisible(true);
//                    emailOtp.setManaged(true);
                }
            }
            riskAssessmentContainer.setVisible(true);
            riskAssessmentContainer.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), riskAssessmentContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    private void clearForm() {
        amountField.clear();
        nameField.clear();
        accountField.clear();
        bankField.clear();
        countryField.clear();
        purposeField.clear();
        currencyComboBox.getSelectionModel().selectFirst();
        sendPaymentRadio.setSelected(true);
        tranPin.setVisible(false);
        tranPin.setManaged(false);
//        smsOtp.setVisible(false);
//        smsOtp.setManaged(false);
        emailOtp.setVisible(false);
        emailOtp.setManaged(false);

        // Hide risk assessment section
        riskAssessmentContainer.setVisible(false);
        riskAssessmentContainer.setManaged(false);

        // Reset buttons
        confirmButton.setDisable(true);
        assessRiskButton.setDisable(false);
        assessRiskButton.setText("Assess Risk");
    }

    /**
     * Validates transaction input fields.
     */
    private boolean validateForm() {
        return !amountField.getText().isEmpty() && !nameField.getText().isEmpty() &&
                !accountField.getText().isEmpty() && !bankField.getText().isEmpty() &&
                !countryField.getText().isEmpty();
    }

    public void handleTransactionConfirmation(ActionEvent event) {
        if (tranPinField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Enter transaction PIN to proceed");
            alert.show();
            return;
        } else if (!tranPinField.getText().equals(DataStore.getInstance().getTransPin())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Incorrect transaction PIN");
            alert.show();
            return;
        }
//        else if (smsOtp.isVisible() && smsOtpField.getText().isEmpty()) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("Enter SMS OTP to proceed");
//            alert.show();
//            return;
//        }
        else if (emailOtp.isVisible() && emailOtpField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Enter email OTP to proceed");
            alert.show();
            return;
        }
//        else if (smsOtp.isVisible() && !smsOtpField.getText().equals(otpSms)) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setContentText("Incorrect SMS OTP");
//            alert.show();
//            return;
//        }
        else if (emailOtp.isVisible() && !emailOtpField.getText().equals(otpEmail)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Incorrect email OTP");
            alert.show();
            return;
        } else {
            String transactionId = generateTransactionID();
            String username = DataStore.getInstance().getUsername();
            double amount = Double.parseDouble(amountField.getText().trim());
            String city = getSenderCity();
            String currency = currencyComboBox.getValue().substring(0, 3).trim();

            DBUtils.addTransaction(username, transactionId, new BigDecimal(amount), city, currency);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Transaction Successful!");
            alert.show();
        }
    }

    /**
     * Shows an alert dialog.
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private static class RiskAssessmentResult {
        String riskLevel;
        String message;

        RiskAssessmentResult(String riskLevel, String message) {
            this.riskLevel = riskLevel;
            this.message = message;
        }
    }

    private String generateTransactionID() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String getSenderCity() {
        try {
            URL url = new URL("https://ipapi.co/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            return json.optString("city", "Unknown");

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
