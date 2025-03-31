package com.example.hackathon;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatbotController {

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox chatContainer;

    @FXML
    private TextField messageInput;

    private final ChatbotService chatbotService = new ChatbotService();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        // Auto-scroll to bottom when new messages are added
        chatContainer.heightProperty().addListener((observable, oldValue, newValue) ->
                chatScrollPane.setVvalue(1.0));
    }

    @FXML
    private void sendMessage() {
        String userMessage = messageInput.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        // Add user message to chat
        addUserMessage(userMessage);

        // Clear input
        messageInput.clear();

        // Process with AI and get response
        chatbotService.processMessage(userMessage, this::addAssistantMessage);
    }

    private void addUserMessage(String message) {
        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add("user-message-container");
        messageContainer.setAlignment(Pos.CENTER_RIGHT);

        VBox messageBox = new VBox();
        messageBox.getStyleClass().add("user-message");

        Label messageText = new Label(message);
        messageText.getStyleClass().add("message-text");
        messageText.setWrapText(true);

        Label messageTime = new Label(getCurrentTime());
        messageTime.getStyleClass().add("message-time");

        messageBox.getChildren().addAll(messageText, messageTime);
        messageContainer.getChildren().add(messageBox);

        chatContainer.getChildren().add(messageContainer);
    }

    private void addAssistantMessage(String message) {
        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add("assistant-message-container");
        messageContainer.setAlignment(Pos.CENTER_LEFT);

        VBox messageBox = new VBox();
        messageBox.getStyleClass().add("assistant-message");

        Label messageText = new Label(message);
        messageText.getStyleClass().add("message-text");
        messageText.setWrapText(true);

        Label messageTime = new Label(getCurrentTime());
        messageTime.getStyleClass().add("message-time");

        messageBox.getChildren().addAll(messageText, messageTime);
        messageContainer.getChildren().add(messageBox);

        chatContainer.getChildren().add(messageContainer);
    }

    private String getCurrentTime() {
        return LocalTime.now().format(timeFormatter);
    }
}