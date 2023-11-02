package com.example.finalproject;

import com.opencsv.CSVReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button SignUpButton;

    private String userPhoneNumber;

    @FXML
    private void handleSignUpButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Register Page");
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) SignUpButton.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String isCredentialsValid(String phoneNumber, String plainTextPassword) {
        String pathToCsv = "src/main/resources/userData.csv";
        try {
            FileReader fileReader = new FileReader(pathToCsv);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> existingData = csvReader.readAll();
            csvReader.close();

            for (String[] data : existingData) {
                if (data.length > 7 && data[7].equals(phoneNumber)) {
                    String storedHashedPassword = data[3];
                    String hashedPassword = hashPasswordMD5(plainTextPassword);

                    if (hashedPassword != null && hashedPassword.equals(storedHashedPassword)) {
                        // Assuming the nationality is stored in the fourth column
                        return data[4]; // Return the user's nationality
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Return null if the credentials are invalid
    }

    private String hashPasswordMD5(String plainTextPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainTextPassword.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showCredentialErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid credentials. Please provide valid phone number and password.");
        alert.showAndWait();
    }

    @FXML
    private void handleLoginButtonClick() {
        String phoneNumber = usernameTextField.getText();
        String plainTextPassword = passwordField.getText();

        if (!phoneNumber.isEmpty() && !plainTextPassword.isEmpty()) {
            String nationality = isCredentialsValid(phoneNumber, plainTextPassword);
            if (nationality != null) {
                userPhoneNumber = phoneNumber;
                openCandidateDetailsScene(nationality);
            } else {
                showCredentialErrorDialog();
            }
        } else {
            showCredentialErrorDialog();
        }
    }

    private void openCandidateDetailsScene(String nationality) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController(); // Get the controller from the loader

            // Assuming you have the user's phone number at this point
            String phoneNumber = usernameTextField.getText();
            profileController.initData(phoneNumber, nationality); // Pass the phone number and nationality to the ProfileController

            Stage stage = new Stage();
            stage.setTitle("Profile page");
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
