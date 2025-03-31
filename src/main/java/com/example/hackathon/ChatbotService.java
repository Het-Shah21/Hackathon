package com.example.hackathon;

import javafx.application.Platform;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class ChatbotService {

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "sk-4072ed376d924697bd1edd82ec5ea149"; // Replace with your actual API key

    private final Map<Pattern, String> responsePatterns = new HashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Thread pool for async calls
    private final HttpClient httpClient = HttpClient.newHttpClient(); // Single HttpClient instance

    public ChatbotService() {
        initializeResponsePatterns();
    }

    private void initializeResponsePatterns() {
        responsePatterns.put(Pattern.compile("(?i).how.*report.*fraud."),
                "To report a fraud, click on the 'Report Fraud' button in the sidebar.");
        responsePatterns.put(Pattern.compile("(?i).suspicious.*transaction."),
                "If you've noticed a suspicious transaction, please report it immediately.");
        responsePatterns.put(Pattern.compile("(?i).what.*fraud.*sign."),
                "Common signs of fraud include unexpected transactions and sudden large withdrawals.");
        responsePatterns.put(Pattern.compile("(?i).how.*system.*work."),
                "FortiFi AI analyzes transaction patterns using machine learning.");
        responsePatterns.put(Pattern.compile("(?i).identity.*theft."),
                "If you suspect identity theft, immediately report it through our system.");
        responsePatterns.put(Pattern.compile("(?i).transaction.*scanner."),
                "Our Transaction Scanner reviews all recent activity for anomalies.");
    }

    public void processMessage(String message, Consumer<String> responseCallback) {
        executorService.submit(() -> { // Run API call in a background thread
            try {
                String response = getResponseForMessage(message);
                Platform.runLater(() -> responseCallback.accept(response)); // Update UI on JavaFX thread
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> responseCallback.accept("Error connecting to AI service."));
            }
        });
    }

    private String getResponseForMessage(String message) throws Exception {
        for (Map.Entry<Pattern, String> entry : responsePatterns.entrySet()) {
            if (entry.getKey().matcher(message).matches()) {
                return entry.getValue();
            }
        }
        return getAIResponse(message); // If no match, call DeepSeek API
    }

    private String getAIResponse(String message) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", new org.json.JSONArray()
                .put(new JSONObject().put("role", "system").put("content", "You are a financial fraud detection chatbot. Help users with financial fraud-related questions. Keep the answers less than 100 words as much as possible."))
                .put(new JSONObject().put("role", "user").put("content", message))
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 🔍 Print the response body to debug format issues
        System.out.println("DeepSeek API Response: " + response.body());

        JSONObject jsonResponse = new JSONObject(response.body());

        // ✅ Safely check for "choices" key
        if (jsonResponse.has("choices")) {
            return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        } else {
            return "DeepSeek API response format unexpected. Check console logs.";
        }
    }
}
