package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;

public class MainPageController {

    @FXML
    private Button pay;

    @FXML
    private void initialize() {
        pay.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hackathon/transaction.fxml"));
                Parent root = loader.load();

                Stage transactionStage = new Stage();
                transactionStage.initOwner(((Node) event.getSource()).getScene().getWindow());
                transactionStage.initModality(Modality.APPLICATION_MODAL);
                transactionStage.setTitle("FortiFi - Secure Transaction");
                transactionStage.setResizable(false);
                transactionStage.setScene(new Scene(root));
                transactionStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @FXML
    private void openChatbot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hackathon/chatbot.fxml"));
            Parent root = loader.load();

            Stage chatbotStage = new Stage();
            chatbotStage.initModality(Modality.APPLICATION_MODAL);
            chatbotStage.setTitle("FortiFi AI - Chatbot");
            chatbotStage.setResizable(false);
            chatbotStage.setScene(new Scene(root));
            chatbotStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openReportFraud() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hackathon/report-fraud.fxml"));
            Parent root = loader.load();

            Stage reportFraudStage = new Stage();
            reportFraudStage.initModality(Modality.APPLICATION_MODAL);
            reportFraudStage.setTitle("FortiFi AI - Report Fraud");
            reportFraudStage.setResizable(false);
            reportFraudStage.setScene(new Scene(root));
            reportFraudStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openTransactionScanner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hackathon/transaction_scanner.fxml"));
            Parent root = loader.load();

            Stage scannerStage = new Stage();
            scannerStage.initModality(Modality.APPLICATION_MODAL);
            scannerStage.setTitle("FortiFi - Transaction Scanner");
            scannerStage.setScene(new Scene(root));
            scannerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}