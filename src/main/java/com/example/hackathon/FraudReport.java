package com.example.hackathon;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FraudReport {
    private String id;
    private String transactionType;
    private LocalDate transactionDate;
    private double transactionAmount;
    private String merchantName;
    private String transactionId;
    private String suspicionReason;
    private String additionalDetails;
    private String contactMethod;
    private String contactDetails;
    private List<File> evidenceFiles;
    private LocalDate reportDate;
    private String status;

    public FraudReport() {
        this.id = UUID.randomUUID().toString();
        this.reportDate = LocalDate.now();
        this.status = "Pending";
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSuspicionReason() {
        return suspicionReason;
    }

    public void setSuspicionReason(String suspicionReason) {
        this.suspicionReason = suspicionReason;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public String getContactMethod() {
        return contactMethod;
    }

    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public List<File> getEvidenceFiles() {
        return evidenceFiles;
    }

    public void setEvidenceFiles(List<File> evidenceFiles) {
        this.evidenceFiles = evidenceFiles;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}