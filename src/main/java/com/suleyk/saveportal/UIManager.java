package com.suleyk.saveportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleListProperty;

import java.io.File;
import java.util.Arrays;

public class UIManager {
    private static final ObservableList<String> gamesList = FXCollections.observableArrayList();

    public static ObservableList<String> getGamesList() {
        return gamesList;
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

            System.out.println("This should be listed in the game list: " + gamesList);
        }
    }
}
