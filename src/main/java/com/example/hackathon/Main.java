package com.example.hackathon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-screen.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setTitle("FortiFi");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
//        primaryStage.getIcons().add(new Image("\"C:\\Users\\K K\\Desktop\\WhatsApp Image 2025-03-31 at 10.44.50_11140cec.jpg\""));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}