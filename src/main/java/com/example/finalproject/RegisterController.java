package com.example.finalproject;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class RegisterController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField middleNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> nationalityComboBox;
    @FXML
    private DatePicker dateOfBirthDatePicker;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Button alreadyRegisterLogIn;

    @FXML
    private void handleAlreadyButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login Page");
            Scene scene = new Scene(root, 400, 400);
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) alreadyRegisterLogIn.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        // Get user input
        String firstName = nameTextField.getText();
        String middleName = middleNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String gender = genderComboBox.getValue();
        String nationality = nationalityComboBox.getValue();
        String dateOfBirth = dateOfBirthDatePicker.getValue() != null ? dateOfBirthDatePicker.getValue().toString() : null;
        String plainTextPassword = passwordField.getText(); // Get the plain-text password
        String phoneNumber = phoneNumberTextField.getText();

        // Hash the password using MD5
        String hashedPassword = hashPasswordMD5(plainTextPassword);

        if (!firstName.isEmpty() && !lastName.isEmpty() && !gender.isEmpty() && !nationality.isEmpty() && !isNullOrEmpty(dateOfBirth) && !hashedPassword.isEmpty() && !phoneNumber.isEmpty()) {
            if (isPhoneNumberUnique(phoneNumber)) {
                // Phone number is unique, proceed with registration
                String pathToCsv = "src/main/resources/userData.csv";
                try {
                    FileWriter fileWriter = new FileWriter(pathToCsv, true);
                    CSVWriter csvWriter = new CSVWriter(fileWriter);
                    String[] csvData = {firstName, middleName, lastName, hashedPassword, nationality, gender, dateOfBirth, phoneNumber}; // Use the hashed password
                    csvWriter.writeNext(csvData);
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Display an error message to the user
                showCredentialErrorDialog();
            }
        } else {
            showCredentialErrorDialog();
        }
    }

    // Check if a string is null or empty
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }



    private void showCredentialErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Please provide valid credentials.");
        alert.showAndWait();
    }

    // Check if the phone number already exists in the CSV file
    private boolean isPhoneNumberUnique(String phoneNumber) {
        String pathToCsv = "src/main/resources/userData.csv";
        try {
            FileReader fileReader = new FileReader(pathToCsv);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> existingData = csvReader.readAll();
            csvReader.close();

            // Check if the provided phone number exists in the CSV data
            for (String[] data : existingData) {
                if (data.length > 7 && data[7].equals(phoneNumber)) {
                    return false; // Phone number already exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true; // Phone number is unique
    }

    // Hash the password using MD5
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
}
