package com.suleyk.saveportal;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class MainController {

    // FXML elements
    @FXML
    private ChoiceBox<String> gameChoiceBox;
    @FXML
    private ChoiceBox<String> profileChoiceBox;
    @FXML
    private Button addProfileButton;
    @FXML
    private Button renameProfileButton;
    @FXML
    private Button deleteProfileButton;
    @FXML
    private Button duplicateProfileButton;
    @FXML
    private ListView<String> backupSavesListView;
    @FXML
    private ToggleButton toggleDarkModeButton;
    @FXML
    private Button importActiveSaveButton;
    @FXML
    private Button exportBackupSaveButton;
    @FXML
    private Button deleteGameButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button closeButton;
    private Scene mainScene;
    // Selected items
    private String selectedGame;
    private String selectedProfile;
    private String selectedBackupSave;

    @FXML
    private void onMinimizeButtonClick() {
        // Get the reference to the stage
        Stage stage = (Stage) minimizeButton.getScene().getWindow();

        // Minimize the stage (minimize the application)
        stage.setIconified(true);
    }

    @FXML
    private void onCloseButtonClick() {
        // Get the reference to the stage
        Stage stage = (Stage) closeButton.getScene().getWindow();

        // Close the stage (close the application)
        stage.close();
    }

    @FXML
    private void onDuplicateProfileButtonClick() throws IOException {
        String selectedGame = gameChoiceBox.getValue();
        String selectedProfile = profileChoiceBox.getValue();

        // Check if a game and profile are selected
        if (selectedGame != null && selectedProfile != null) {
            // Define the source and destination paths
            String sourcePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile).toString();
            String duplicateProfile = selectedProfile + "_Copy";  // Modify as needed
            String destinationPath = Paths.get(FileUtils.backupFolderPath, selectedGame, duplicateProfile).toString();

            // Copy the profile directory
            FileUtils.copyFolder(sourcePath, destinationPath);

            // Update the backups list
            UIManager.populateBackupSavesList(selectedGame, duplicateProfile);

            // Update the profile list
            UIManager.populateProfileList(selectedGame);

            // Select the duplicated profile in the choice box
            profileChoiceBox.setValue(duplicateProfile);
        }
    }

    @FXML
    private void onOpenBackupsFolderButtonClick() {
        String backupFolderPath = FileUtils.backupFolderPath;
        if (backupFolderPath != null) {
            FileUtils.openFolder(backupFolderPath);
        } else {
            System.err.println("Backup folder path is not set.");
        }
    }
    public ToggleButton getToggleDarkModeButton() {
        return toggleDarkModeButton;
    }

    // Initialize method called when the UI is loaded
    public void initialize() {
        // Disable the profile buttons if no game is selected (and delete game button)
        addProfileButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());
        renameProfileButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());
        deleteProfileButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());
        duplicateProfileButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());
        profileChoiceBox.disableProperty().bind(gameChoiceBox.valueProperty().isNull());
        deleteGameButton.disableProperty().bind(gameChoiceBox.valueProperty().isNull());

        // Disable buttons to manage profiles if no profile is selected
        renameProfileButton.disableProperty().bind(profileChoiceBox.valueProperty().isNull());
        deleteProfileButton.disableProperty().bind(profileChoiceBox.valueProperty().isNull());
        duplicateProfileButton.disableProperty().bind(profileChoiceBox.valueProperty().isNull());

        // Disable import and export buttons when no profile or save is selected
        importActiveSaveButton.disableProperty().bind(profileChoiceBox.valueProperty().isNull());
        exportBackupSaveButton.disableProperty().bind(profileChoiceBox.valueProperty().isNull().or(backupSavesListView.getSelectionModel().selectedItemProperty().isNull()));

        profileChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Clear the backups list when the selected profile changes
            backupSavesListView.getItems().clear();
        });

        // Update backups list when importing
        importActiveSaveButton.setOnMouseClicked(event -> UIManager.populateBackupSavesList(selectedGame, selectedProfile));

        // Set up event handlers and populate initial data for gameChoiceBox
        gameChoiceBox.setItems(UIManager.getGamesList());
        gameChoiceBox.setOnMouseClicked(event -> UIManager.populateGamesList());
        gameChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleSelectedGameChange(newValue));

        // Set up event handlers and populate initial data for profileChoiceBox
        profileChoiceBox.setItems(UIManager.getProfilesList());
        profileChoiceBox.setOnMouseClicked(event -> UIManager.populateProfileList(selectedGame));
        profileChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleSelectedProfileChange(newValue));

        // Set up event handlers and populate initial data for backupSavesListView
        backupSavesListView.setItems(UIManager.getBackupSavesList());
        backupSavesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleSelectedBackupSaveChange(newValue));

        // Set up the context menu for backupSavesListView
        backupSavesListView.setContextMenu(createBackupSavesContextMenu());


    }

    // Method to create the context menu for backupSavesListView
    private ContextMenu createBackupSavesContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Menu items for renaming and deleting a backup save
        MenuItem renameMenuItem = new MenuItem("Rename");
        MenuItem deleteMenuItem = new MenuItem("Delete");

        // Set actions for the menu items
        renameMenuItem.setOnAction(event -> onRenameBackupSave());
        deleteMenuItem.setOnAction(event -> onDeleteBackupSave());

        // Add menu items to the context menu
        contextMenu.getItems().addAll(renameMenuItem, deleteMenuItem);

        return contextMenu;
    }

    // Method to handle renaming a backup save
    private void onRenameBackupSave() {
        // Check if a backup save is selected
        if (selectedBackupSave != null) {
            // Display an input dialog to get the new backup save name
            TextInputDialog dialog = new TextInputDialog(selectedBackupSave);
            dialog.setTitle("Rename Backup Save");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter the new name for the backup save:");

            UIManager.styleDialog(dialog);


            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newBackupSaveName -> {
                // Validate if the new backup save name is not empty
                if (!newBackupSaveName.trim().isEmpty()) {
                    // Build the path for the old backup save folders
                    String oldBackupSavePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile, selectedBackupSave).toString();

                    // Rename the backup save
                    FileUtils.rename(oldBackupSavePath, newBackupSaveName);

                    // Update the UI
                    UIManager.populateBackupSavesList(selectedGame, selectedProfile);

                    // Optionally, show a confirmation message
                    showInfoAlert("Backup Save Renamed", "Backup save renamed successfully!");
                } else {
                    showErrorAlert("Error", "Backup save name cannot be empty.");
                }
            });
        } else {
            showErrorAlert("Error", "Please select a backup save first.");
        }
    }

    // Method to handle deleting a backup save
    private void onDeleteBackupSave() {
        // Check if a backup save is selected
        if (selectedBackupSave != null) {
            // Display a confirmation dialog to ensure the user wants to delete the backup save
            boolean userConfirmed = showConfirmationDialog("Delete Backup Save", "Are you sure you want to delete the backup save '" + selectedBackupSave + "'?");

            if (userConfirmed) {
                // Build the path for the backup save folder
                String backupSavePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile, selectedBackupSave).toString();

                // Delete the backup save
                FileUtils.deleteFolder(new File(backupSavePath));

                // Update the UI
                UIManager.populateBackupSavesList(selectedGame, selectedProfile);

                // Optionally, show a confirmation message
                showInfoAlert("Backup Save Deleted", "Backup save deleted successfully!");
            }
        } else {
            showErrorAlert("Error", "Please select a backup save first.");
        }
    }

    // Helper method to show an information alert
    private void showInfoAlert(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    // Helper method to show an error alert
    private void showErrorAlert(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    // Helper method to show a confirmation dialog
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        UIManager.styleDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Helper method to show an alert of a specified type
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        UIManager.styleDialog(alert);

        alert.showAndWait();
    }

    // Method to handle a change in the selected game
    private void handleSelectedGameChange(String newGame) {
        selectedGame = newGame;
        System.out.println("Selected Game: " + newGame);
        profileChoiceBox.getSelectionModel().clearSelection();
        UIManager.populateProfileList(selectedGame);
        handleSelectedProfileChange(null);
    }

    // Method to handle a change in the selected profile
    private void handleSelectedProfileChange(String newProfile) {
        selectedProfile = newProfile;
        System.out.println("Selected Profile: " + newProfile);
        UIManager.populateBackupSavesList(selectedGame, selectedProfile);
        backupSavesListView.setItems(UIManager.getBackupSavesList());
    }

    // Method to handle a change in the selected backup save
    private void handleSelectedBackupSaveChange(String newBackupSave) {
        selectedBackupSave = newBackupSave;
        System.out.println("Selected BackupSave: " + newBackupSave);
    }

    // Event handler for the "Add Game" button
    @FXML
    private void onAddGameButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Active Save Path");
        File activeSavePath = directoryChooser.showDialog(null);

        if (activeSavePath != null) {
            TextInputDialog dialog = new TextInputDialog();
            UIManager.styleDialog(dialog);


            dialog.setTitle("Game Name");
            dialog.setHeaderText("Enter the name for the new game:");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(gameName -> addGame(gameName, activeSavePath.getAbsolutePath()));

            UIManager.populateGamesList();
        }
    }

    // Event handler for the "Add Profile" button
    @FXML
    private void onAddProfileButtonClick() {
        if (selectedGame != null) {
            TextInputDialog dialog = new TextInputDialog();

            UIManager.styleDialog(dialog);

            dialog.setTitle("Profile Name");
            dialog.setHeaderText("Enter the name for the new profile:");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(profileName -> {
                FileUtils.addProfile(selectedGame, profileName);
                UIManager.populateProfileList(selectedGame);
                profileChoiceBox.setValue(profileName);
            });

        } else {
            System.err.println("Please select a game first.");
        }
    }

    // Method to add a new game
    private void addGame(String gameName, String activeSavePath) {
        FileUtils.addGame(gameName, activeSavePath);
        FileUtils.addProfile(gameName, "DefaultProfile");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        UIManager.styleDialog(alert);
        alert.setTitle("Game Added");
        alert.setHeaderText(null);
        alert.setContentText("Game '" + gameName + "' added successfully!");
        alert.showAndWait();

        gameChoiceBox.setValue(gameName);
    }

    // Method to get the active save path for the selected game
    private String getSelectedGameActiveSavePath() {
        if (selectedGame != null) {
            String activeSavePathIniPath = Paths.get(FileUtils.backupFolderPath, selectedGame, "activeSavePath.ini").toString();
            return FileUtils.readIniFile(activeSavePathIniPath, "activeSavePath");
        } else {
            System.err.println("Please select a game first.");
            return null;
        }
    }

    // Event handler for the "Import Active Save" button
    @FXML
    private void onImportActiveSaveButtonClick() throws IOException {
        if (selectedGame != null && selectedProfile != null) {
            String sourcePath = getSelectedGameActiveSavePath();
            System.out.println("Source Path: " + sourcePath);

            if (sourcePath != null) {
                String destinationFolder = FileUtils.generateUniqueFolderName(Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile).toString(), "ImportedSave");
                FileUtils.copyFolder(sourcePath, destinationFolder);

                System.out.println("Import successful! Contents copied to: " + destinationFolder);
            } else {
                System.err.println("Error reading activeSavePath.ini from the selected game.");
            }
        } else {
            System.err.println("Please select a game and a profile first.");
        }
    }

    @FXML
    private Label exportFeedbackLabel;

    // Event handler for the "Export Backup Save" button
    @FXML
    private void onExportBackupSaveButtonClick() throws IOException {
        if (selectedGame != null && selectedProfile != null && selectedBackupSave != null) {
            String sourcePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile, selectedBackupSave).toString();
            System.out.println("Source Path: " + sourcePath);

            String destinationPath = getSelectedGameActiveSavePath();

            if (destinationPath != null) {
                FileUtils.copyFolder(sourcePath, destinationPath);

                // Show export feedback message
                showExportFeedback("Backup save exported successfully!");

                System.out.println("Export successful! Contents copied to: " + destinationPath);
            } else {
                System.err.println("Error reading activeSavePath.ini from the selected game.");
            }
        } else {
            System.err.println("Please select a game, profile, and backup save first.");
        }
    }

    private void showExportFeedback(String message) {
        exportFeedbackLabel.setText(message);
        exportFeedbackLabel.setVisible(true);

        // Set up a timeline to hide the label after a short delay (e.g., 3 seconds)
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> exportFeedbackLabel.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }


    // Event handler for the "Delete Game" button
    @FXML
    private void onDeleteGameButtonClick() {
        if (selectedGame != null) {
            boolean userConfirmed = showConfirmationDialog("Delete Game", "Are you sure you want to delete the game '" + selectedGame + "'?");

            if (userConfirmed) {
                File gameFolder = new File(FileUtils.backupFolderPath, selectedGame);
                FileUtils.deleteFolder(gameFolder);

                System.out.println("Game deleted: " + selectedGame);

                UIManager.populateGamesList();
                gameChoiceBox.setValue(null);

            }
        } else {
            System.err.println("Please select a game first.");
        }
    }

    // Event handler for the "Delete Profile" button
    @FXML
    private void onDeleteProfileButtonClick() {
        if (selectedProfile != null) {
            boolean userConfirmed = showConfirmationDialog("Delete Profile", "Are you sure you want to delete the profile '" + selectedProfile + "'?");

            if (userConfirmed) {
                Path profilePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile);
                FileUtils.deleteFolder(profilePath.toFile());

                System.out.println("Profile deleted: " + selectedProfile);

                // Clear the profile list
                profileChoiceBox.getItems().clear();

                // Clear the backup list
                backupSavesListView.getItems().clear();

                // Update the backup list
                UIManager.populateProfileList(selectedGame);

            }
        } else {
            System.err.println("Please select a profile first.");
        }
    }

    // Event handler for the "Rename Profile" button
    @FXML
    private void onRenameProfileButtonClick() {
        if (selectedProfile != null) {
            TextInputDialog dialog = new TextInputDialog(selectedProfile);
            dialog.setTitle("Rename Profile");
            dialog.setHeaderText("Enter the new name for the profile:");
            //dialog.setContentText("Enter the new name for the profile:");

            UIManager.styleDialog(dialog);


            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newProfileName -> {
                if (!newProfileName.trim().isEmpty()) {
                    Path newProfilePath = Paths.get(FileUtils.backupFolderPath, selectedGame, newProfileName);
                    if (Files.exists(newProfilePath)) {
                        System.err.println("Error: Profile with the name '" + newProfileName + "' already exists.");
                    } else {
                        Path oldProfilePath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile);
                        try {
                            Files.move(oldProfilePath, newProfilePath);
                            System.out.println("Profile renamed from '" + selectedProfile + "' to '" + newProfileName + "'.");
                            selectedProfile = newProfileName;
                            UIManager.populateProfileList(selectedGame);
                            profileChoiceBox.setValue(newProfileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.err.println("Error renaming profile.");
                        }
                    }
                } else {
                    System.err.println("Error: Profile name cannot be empty.");
                }
            });
        } else {
            System.err.println("Please select a profile first.");
        }
    }
}
