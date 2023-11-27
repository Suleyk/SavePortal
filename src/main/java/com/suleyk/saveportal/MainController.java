package com.suleyk.saveportal;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Optional;

public class MainController {

    @FXML
    private ChoiceBox<String> gameChoiceBox;

    @FXML
    private ChoiceBox<String> profileChoiceBox;

    @FXML
    private Button addProfileButton;


    public void initialize() {


        addProfileButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());


        // Bind the ChoiceBox items to the games list in UIManager
        gameChoiceBox.setItems(UIManager.getGamesList());
        gameChoiceBox.setOnMouseClicked(event -> {
            UIManager.populateGamesList();
        });

        gameChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Handle the selected game change here
            handleSelectedGameChange(newValue);
        });

    }

    private void handleSelectedGameChange(String newGame) {
        // TODO: Implement the logic for handling the selected game change
        // For now, let's just print the selected game for testing
        System.out.println("Selected Game: " + newGame);
    }

    @FXML
    private void onAddGameButtonClick() {
        // Open a DirectoryChooser for the user to select the live save path
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Live Save Path");
        File liveSavePath = directoryChooser.showDialog(null);

        if (liveSavePath != null) {
            // Ask the user for the game name
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Game Name");
            dialog.setHeaderText("Enter the name for the new game:");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(gameName -> {
                // Call a method to add the game with the provided name and live save path
                addGame(gameName, liveSavePath.getAbsolutePath());
            });
        }
    }

    @FXML
    private void onAddProfileButtonClick() {
        // Get the selected game
        String selectedGame = gameChoiceBox.getValue();

        if (selectedGame != null) {
            // Call a method to add a profile for the selected game
            //addProfile(selectedGame);
        }
    }

    private void addGame(String gameName, String liveSavePath) {
        // Call a method in FileUtils to handle adding the game
        // Pass the game name, live save path, and any additional parameters as needed
        FileUtils.addGame(gameName, liveSavePath);

        // Display a confirmation message or handle errors if needed
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Added");
        alert.setHeaderText(null);
        alert.setContentText("Game '" + gameName + "' added successfully!");
        alert.showAndWait();
    }


}