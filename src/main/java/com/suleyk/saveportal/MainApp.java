package com.suleyk.saveportal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the main FXML file
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);

        primaryStage.setTitle("SavePortal");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize the application data and UI
        initializeApplication();

        // Make stage draggable method call
        UIManager.makeStageDraggable(primaryStage, scene);
    }

    private void initializeApplication() {
        // Create necessary folders and files
        FileUtils.createSavePortalFolder();
        FileUtils.createBackupFolder();
        FileUtils.createConfigFile();

        // Populate the initial list of games in the UI
        UIManager.populateGamesList();


    }
}
