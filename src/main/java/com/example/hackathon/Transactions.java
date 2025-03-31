package com.example.hackathon;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transactions {
    private String username;
    private String transactionId;
    private BigDecimal amount;
    private Timestamp transactionTime;
    private String location;
    private String currency;

    public Transactions(String username, String transactionId, BigDecimal amount, Timestamp transactionTime, String location, String currency) {
        this.username = username;
        this.transactionId = transactionId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.location = location;
        this.currency = currency;
    }

    public String getUsername() {
        return username;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "username='" + username + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", transactionTime=" + transactionTime +
                ", location='" + location + '\'' +
                '}';
    }
}
