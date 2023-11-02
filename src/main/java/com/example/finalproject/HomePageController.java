package com.example.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class HomePageController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;
    @FXML
    private void handleLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login Page");
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegisterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registration Page");
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
