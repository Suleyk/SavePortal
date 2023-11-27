package com.suleyk.saveportal;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

public class FileUtils {

    public static String savePortalFolderPath = Paths.get(System.getProperty("user.home"), "SavePortal").toString();
    public static String backupFolderPath = Paths.get(savePortalFolderPath, "Backups").toString();
    public static String configFilePath = Paths.get(savePortalFolderPath, "config.ini").toString();


    public static void createSavePortalFolder() {
        createFolder(savePortalFolderPath);
    }

    public static void createBackupFolder() {
        createFolder(backupFolderPath);
    }

    public static void createConfigFile() {
        createFile(configFilePath);
    }

    private static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Folder created: " + folderPath);
            } else {
                System.err.println("Failed to create folder: " + folderPath);
            }
        } else {
            System.out.println("Folder already exists: " + folderPath);
        }
    }

    private static void createFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + filePath);
                } else {
                    System.err.println("Failed to create file: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error creating file: " + filePath);
            }
        } else {
            System.out.println("File already exists: " + filePath);
        }
    }

    public static void copyFolder(String sourcePath, String destinationPath) {

        File sourceFolder = new File(sourcePath);
        File destinationFolder = new File(destinationPath);

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.err.println("Source folder does not exist: " + sourcePath);
            return;
        }

        if (!destinationFolder.exists()) {
            deleteFolder(destinationFolder);
            createFolder(destinationPath);
        }

        File[] sourceContents = sourceFolder.listFiles();
        for (File sourceContent : sourceContents) {
            String destinationContentPath = destinationPath + File.separator + sourceContent.getName();

            if (sourceContent.isFile()) {
                copyFileContent(sourceContent, new File(destinationContentPath));
            } else if (sourceContent.isDirectory()) {
                copyFolder(sourceContent.getAbsolutePath(), destinationContentPath);
            }
        }

    }


    private static void deleteFolder(File folder) {
        try {
            Path folderPath = folder.toPath();
            Files.walk(folderPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            System.out.println("Folder deleted: " + folder.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting the folder: " + folder.getAbsolutePath());
        }

    }


    private static void copyFileContent(File sourceFile, File destinationFile) {
        try {
            Path sourcePath = sourceFile.toPath();
            Path destinationPath = destinationFile.toPath();

            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File content copied from " + sourceFile.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error copying file content: " + e.getMessage());
        }

    }

    public static void rename(String currentPath, String newName) {
        File currentFile = new File(currentPath);
        if (!currentFile.exists()) {
            System.err.println("Cannot find file or folder at: " + currentPath);
            return;
        }

        String parentPath = currentFile.getParent();
        String newFilePath = parentPath + File.separator + newName;

        File newFile = new File(newFilePath);
        if (newFile.exists()) {
            System.err.println("A file or folder with the name '" + newName + "' already exists at " + newFilePath);
            return;
        }

        if (currentFile.renameTo(newFile)) {
            System.out.println("Successfully renamed to: " + newFilePath);
        } else {
            System.err.println("Failed to rename file or folder.");
        }
    }

    public static void addGame(String gameName, String liveSavePath) {
        // Create the game folder inside the Backups folder
        String gameFolderPath = Paths.get(backupFolderPath, gameName).toString();
        createFolder(gameFolderPath);

        // Create an ini file for the game
        String gameIniPath = Paths.get(gameFolderPath, "liveSavePath.ini").toString();
        createFile(gameIniPath);

        // Write the live save path to the ini file
        writeLiveSavePathToIni(gameIniPath, liveSavePath);
    }

    private static void writeLiveSavePathToIni(String iniPath, String liveSavePath) {
        try {
            Path path = Path.of(iniPath);
            Files.writeString(path, "liveSavePath=" + liveSavePath, StandardOpenOption.CREATE);
            System.out.println("Live save path written to: " + iniPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing live save path to ini: " + e.getMessage());
        }
    }


}



