package com.example.hackathon;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String senderId;
    private String receiverId;
    private double amount;
    private String currency;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String senderCity; // New field

    public Transaction(String id, String senderId, String receiverId,
                       double amount, String currency, String senderIp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.COMPLETED;
        this.senderCity = fetchCityFromIP(senderIp); // Fetch sender's city
    }

    private String fetchCityFromIP(String ip) {
        try {
            String url = "http://ip-api.com/json/" + ip;
            java.net.URL obj = new java.net.URL(url);
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Extract city from JSON response
            org.json.JSONObject jsonResponse = new org.json.JSONObject(response.toString());
            return jsonResponse.getString("city");
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    // Getters and setters
    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getSenderCity() { return senderCity; }
}

enum TransactionStatus {
    PENDING, COMPLETED, FAILED, FLAGGED
}