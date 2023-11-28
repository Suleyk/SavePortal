package com.suleyk.saveportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

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
        String backupsFolderPath = FileUtils.backupFolderPath;

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
        String selectedGameFolderPath = Paths.get(FileUtils.backupFolderPath, selectedGame).toString();

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
        String selectedProfileFolderPath = Paths.get(FileUtils.backupFolderPath, selectedGame, selectedProfile).toString();
        File selectedProfileFolder = new File(selectedProfileFolderPath);

        // Get the list of subfolders inside the selected profile folder
        File[] subfolders = selectedProfileFolder.listFiles(File::isDirectory);

        if (subfolders != null) {
            // Extract folder names and update the backup saves list
            String[] folderNames = Arrays.stream(subfolders).map(File::getName).toArray(String[]::new);
            backupSavesList.setAll(folderNames);
        }
    }


}