package com.example.hackathon;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailOTPService {
    private static final String EMAIL = "way4hell6@gmail.com"; // Your email
    private static final String PASSWORD = "boow sofe iukl iktu"; // Your App Password

    public static void sendOTP(String recipientEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp + ". It will expire in 5 minutes.");

            Transport.send(message);
            System.out.println("OTP sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}