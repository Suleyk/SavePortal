package com.suleyk.saveportal;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.awt.Desktop;
public class FileUtils {

    public static final String SAVEPORTAL_FOLDER_PATH = Paths.get(System.getProperty("user.home"), "SavePortal").toString();
    public static final String BACKUPS_FOLDER_PATH = Paths.get(SAVEPORTAL_FOLDER_PATH, "Backups").toString();
    public static final String CONFIGFILE_PATH = Paths.get(SAVEPORTAL_FOLDER_PATH, "config.ini").toString();

    public static void createSavePortalFolder() {
        createFolder(SAVEPORTAL_FOLDER_PATH);
    }

    public static void createBackupFolder() {
        createFolder(BACKUPS_FOLDER_PATH);
    }

    public static void createConfigFile() {
        createFile(CONFIGFILE_PATH);
    }

    public static void copyFolder(String sourcePath, String destinationPath) throws IOException {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);

            // If the destination folder exists, delete it
            if (Files.exists(destination)) {
                deleteFolder(destination.toFile());
            }

            // Copy the source folder to the destination
            Files.walk(source)
                    .forEach(sourceFile -> {
                        Path destinationFile = destination.resolve(source.relativize(sourceFile));
                        try {
                            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            throw new RuntimeException("Error copying file: " + sourceFile, e);
                        }
                    });
        } catch (IOException e) {
            throw new IOException("Error copying folder: " + sourcePath, e);
        }
    }

    public static void deleteFolder(File folder) {
        try {
            if (folder.exists()) {
                Path folderPath = folder.toPath();
                Files.walk(folderPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(FileUtils::deleteFile);

                System.out.println("Folder deleted: " + folder.getAbsolutePath());
            } else {
                System.out.println("Folder does not exist: " + folder.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting the folder: " + folder.getAbsolutePath());
        }
    }

    private static void deleteFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error deleting file: " + file.getAbsolutePath());
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

    public static void addGame(String gameName, String activeSavePath) {
        // Create the game folder inside the Backups folder
        String gameFolderPath = Paths.get(BACKUPS_FOLDER_PATH, gameName).toString();
        createFolder(gameFolderPath);

        // Create an ini file for the game
        String gameIniPath = Paths.get(gameFolderPath, "activeSavePath.ini").toString();
        createFile(gameIniPath);

        // Write the active save path to the ini file
        writeActiveSavePathToIni(gameIniPath, activeSavePath);
    }

    private static void writeActiveSavePathToIni(String iniPath, String activeSavePath) {
        try {
            Path path = Path.of(iniPath);
            Files.writeString(path, "activeSavePath=" + activeSavePath, StandardOpenOption.CREATE);
            System.out.println("Active save path written to: " + iniPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing active save path to ini: " + e.getMessage());
        }
    }

    public static void addProfile(String gameName, String profileName) {
        String gameFolderPath = Paths.get(BACKUPS_FOLDER_PATH, gameName).toString();
        String profileFolderPath = Paths.get(gameFolderPath, profileName).toString();
        createFolder(profileFolderPath);
    }

    public static String generateUniqueFolderName(String basePath, String baseName) {
        // Initialize variables
        String newName;
        int index = 0;
        File folder;

        // Start a do-while loop to generate unique folder names
        do {
            // Create the new folder name by combining basePath, baseName, and a formatted index
            newName = Paths.get(basePath, baseName + String.format("%02d", index++)).toString();

            // Check if a folder with the generated name already exists
            folder = new File(newName);
        } while (folder.exists());  // Continue the loop while the folder exists

        // Return the unique folder name
        return newName;
    }

    public static String readIniFile(String filePath, String key) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.startsWith(key + "=")) {
                    // Return the value part of the line
                    return line.substring(key.length() + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading INI file: " + filePath);
        }
        return null;
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



    public static void openFolder(String folderPath) {
        try {
            // Create a File object for the folder
            File folder = new File(folderPath);

            // Check if the Desktop is supported and the folder exists
            if (Desktop.isDesktopSupported() && folder.exists()) {
                // Open the folder with the default file manager
                Desktop.getDesktop().open(folder);
            } else {
                System.out.println("Desktop not supported or folder does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening folder: " + e.getMessage());
        }
    }


}
