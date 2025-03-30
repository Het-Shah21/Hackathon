package com.example.hackathon;

public class UpiIdGenerator {

    private UpiIdGenerator() {}

    public static String generateUpiId(String phoneNumber, String bank) {
        String upiBank = switch (bank.toLowerCase()) {
            case "state bank of india" -> "sbi";
            case "abc bank" -> "abc";
            default -> null;
        };
        return phoneNumber + "@ok" + upiBank;
    }
}
