package com.example.hackathon;

public class User {
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String upiID;
    private String transPin;
    private String authPin;

    public User(String username) {
        this.username = username;

    }

    public User(String name, String username, String password, String email, String phone, String upiID, String transPin, String authPin) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.upiID = upiID;
        this.transPin = transPin;
        this.authPin = authPin;
    }
}
