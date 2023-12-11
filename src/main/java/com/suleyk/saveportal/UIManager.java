package com.suleyk.saveportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class UIManager {
    // ObservableLists to store data for UI elements
    private static final ObservableList<String> gamesList = FXCollections.observableArrayList();
    private static final ObservableList<String> profilesList = FXCollections.observableArrayList();
    private static final ObservableList<String> backupSavesList = FXCollections.observableArrayList();

    public static ObservableList<String> getGamesList() {
        return gamesList;
    }

    public static ObservableList<String> getProfilesList() {
        return profilesList;
    }

    public static ObservableList<String> getBackupSavesList() {
        return backupSavesList;
    }

    // Method to dynamically populate the games list
    public static void populateGamesList() {
        // Define the path to the Backups folder
        String backupsFolderPath = FileUtils.BACKUPS_FOLDER_PATH;

        // Get the list of folders inside the Backups folder
        File backupsFolder = new File(backupsFolderPath);
        File[] folders = backupsFolder.listFiles(File::isDirectory);

        if (folders != null) {
            // Extract folder names and update the games list
            String[] folderNames = Arrays.stream(folders).map(File::getName).toArray(String[]::new);
            gamesList.setAll(folderNames);

            // Debugging print statement
            System.out.println("Games list updated: " + gamesList);
        }
    }

    // Method to populate the profile list based on the selected game
    public static void populateProfileList(String selectedGame) {
        if (selectedGame == null) {
            return;
        }

        // Define the path to the selected profile folder
        String selectedGameFolderPath = Paths.get(FileUtils.BACKUPS_FOLDER_PATH, selectedGame).toString();

        // Get the list of folders inside the selected game folder
        File selectedGameFolder = new File(selectedGameFolderPath);
        File[] folders = selectedGameFolder.listFiles(File::isDirectory);

        if (folders != null) {
            // Extract folder names and update the profile list
            String[] folderNames = Arrays.stream(folders).map(File::getName).toArray(String[]::new);
            profilesList.setAll(folderNames);

            // Debugging print statement
            System.out.println("Profile list updated: " + profilesList);
        }
    }

    // Method to populate the backup saves list based on the selected game and profile
    public static void populateBackupSavesList(String selectedGame, String selectedProfile) {
        if (selectedGame == null || selectedProfile == null) {
            return;
        }

        // Define the path to the selected profile folder
        String selectedProfileFolderPath = Paths.get(FileUtils.BACKUPS_FOLDER_PATH, selectedGame, selectedProfile).toString();
        File selectedProfileFolder = new File(selectedProfileFolderPath);

        // Get the list of subfolders inside the selected profile folder
        File[] subfolders = selectedProfileFolder.listFiles(File::isDirectory);

        if (subfolders != null) {
            // Sort subfolders based on the last modified timestamp of the first file in each folder
            Arrays.sort(subfolders, Comparator.comparingLong(folder -> {
                File[] files = folder.listFiles();
                if (files != null && files.length > 0) {
                    return files[0].lastModified();
                } else {
                    return 0L;
                }
            }));

            // Extract folder names and update the backup saves list
            String[] folderNames = Arrays.stream(subfolders).map(File::getName).toArray(String[]::new);
            backupSavesList.setAll(folderNames);
        }
    }

    // Method to make the stage draggable
    public static void makeStageDraggable(Stage stage, Scene scene) {
        double[] xOffset = {0};
        double[] yOffset = {0};

        // Set up event handlers for mouse press, drag, and release
        scene.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            double x = event.getScreenX() - xOffset[0];
            double y = event.getScreenY() - yOffset[0];

            stage.setX(x);
            stage.setY(y);
        });
    }

    public static void styleDialog(Dialog<?> dialog) {
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                UIManager.class.getResource("dark-mode.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        // Set the StageStyle to TRANSPARENT
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialog.setGraphic(new ImageView()); // Empty ImageView
        UIManager.makeStageDraggable(dialogStage, dialog.getDialogPane().getScene());
    }


}
