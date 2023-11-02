package com.example.finalproject;

import com.opencsv.CSVReader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ProfileController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label nationalityLabel;
    @FXML
    private ImageView flagImageView;
    @FXML
    private Label genderLabel;

    public void initData(String phoneNumber, String nationality) {
        String pathToCsv = "src/main/resources/userData.csv";
        try {
            FileReader fileReader = new FileReader(pathToCsv);
            CSVReader csvReader = new CSVReader(fileReader);
            List<String[]> existingData = csvReader.readAll();
            csvReader.close();

            for (String[] data : existingData) {
                if (data.length > 7 && data[7].equals(phoneNumber)) {
                    String fullName = data[0] + " " + data[2];
                    String gender = data[5];
                    String flagImagePath = "";

                    switch (nationality) {
                        case "Malaysia":
                            flagImagePath = "malaysia.png";
                            break;
                        case "Singapore":
                            flagImagePath = "singapore_flag.png";
                            break;
                        case "Thailand":
                            flagImagePath = "thailand_flag.png";
                            break;
                        default:
                            // Provide a default flag path or handle the case where the nationality is not recognized
                            break;
                    }

                    nameLabel.setText(fullName);
                    nationalityLabel.setText(nationality);
                    genderLabel.setText(gender);

                    // Load the image only if the path is valid
                    if (isValidImageURL(flagImagePath)) {
                        flagImageView.setImage(new Image(flagImagePath));
                    } else {
                        // Provide a default image or handle the error appropriately
                        System.out.println("Invalid image path: " + flagImagePath);
                    }

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if the URL for the image is valid
    private boolean isValidImageURL(String url) {
        try {
            new Image(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
