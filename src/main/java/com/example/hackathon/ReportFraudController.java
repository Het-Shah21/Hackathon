package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ReportFraudController {

    @FXML private ComboBox<String> transactionTypeCombo;
    @FXML private DatePicker transactionDatePicker;
    @FXML private TextField transactionAmountField;
    @FXML private TextField merchantNameField;
    @FXML private TextField transactionIdField;
    @FXML private ComboBox<String> suspicionReasonCombo;
    @FXML private TextArea additionalDetailsArea;
    @FXML private ComboBox<String> contactMethodCombo;
    @FXML private TextField contactDetailsField;
    @FXML private Label fileSelectedLabel;
    @FXML private CheckBox consentCheckBox;
    @FXML private Button submitButton;

    private List<File> selectedFiles;

    @FXML
    private void initialize() {
        // Initialize combo box options
        transactionTypeCombo.getItems().addAll(
                "Debit Card", "Credit Card", "Bank Transfer", "Online Payment", "ATM Withdrawal", "Other"
        );

        suspicionReasonCombo.getItems().addAll(
                "Unauthorized Transaction", "Double Charge", "Incorrect Amount",
                "Merchant Dispute", "Suspicious Activity", "Other"
        );

        contactMethodCombo.getItems().addAll(
                "Email", "Phone Call", "SMS", "In-app Notification"
        );

        // Set default date to today
        transactionDatePicker.setValue(LocalDate.now());

        // Bind submit button to consent checkbox
        consentCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(!newValue);
        });
    }

    @FXML
    private void chooseFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Evidence Files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "."),
                new FileChooser.ExtensionFilter("Images", ".png", ".jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PDF Documents", "*.pdf")
        );

        selectedFiles = fileChooser.showOpenMultipleDialog(fileSelectedLabel.getScene().getWindow());

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            fileSelectedLabel.setText(selectedFiles.size() + " file(s) selected");
        } else {
            fileSelectedLabel.setText("No files selected");
        }
    }

    @FXML
    private void handleSubmit() {
        // Validate form data
        if (validateForm()) {
            // Submit form data to backend
            submitFormData();

            // Show confirmation and reset form
            showConfirmation();
            resetForm();
        }
    }

    @FXML
    private void handleCancel() {
        // Reset form or navigate back
        resetForm();
    }

    private boolean validateForm() {
        // Add validation logic here
        // This is a basic example - you would add more comprehensive validation
        boolean isValid = true;

        if (transactionTypeCombo.getValue() == null ||
                transactionDatePicker.getValue() == null ||
                transactionAmountField.getText().isEmpty() ||
                merchantNameField.getText().isEmpty() ||
                suspicionReasonCombo.getValue() == null ||
                contactMethodCombo.getValue() == null ||
                contactDetailsField.getText().isEmpty()) {

            isValid = false;
            // Show error message or highlight fields
        }

        return isValid;
    }

    private void submitFormData() {
        // Logic to submit form data to backend
        System.out.println("Submitting fraud report...");
        // In a real application, you would send this data to your server
    }

    private void showConfirmation() {
        // Show confirmation message to user
        // For a full application, you might show a dialog or navigate to a confirmation page
        System.out.println("Report submitted successfully!");
    }

    private void resetForm() {
        // Reset all form fields
        transactionTypeCombo.setValue(null);
        transactionDatePicker.setValue(LocalDate.now());
        transactionAmountField.clear();
        merchantNameField.clear();
        transactionIdField.clear();
        suspicionReasonCombo.setValue(null);
        additionalDetailsArea.clear();
        contactMethodCombo.setValue(null);
        contactDetailsField.clear();
        fileSelectedLabel.setText("No files selected");
        consentCheckBox.setSelected(false);
        selectedFiles = null;
    }
}