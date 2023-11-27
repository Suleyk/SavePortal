package com.suleyk.saveportal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the main FXML file
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("SavePortal");
        primaryStage.setScene(scene);
        primaryStage.show();

        FileUtils.createSavePortalFolder();
        FileUtils.createBackupFolder();
        FileUtils.createConfigFile();
        UIManager.populateGamesList();
    }

}
