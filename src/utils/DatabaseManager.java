package utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager class for handling file-based data persistence
 */
public class DatabaseManager {
    private static final String DATA_DIRECTORY = "data";
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        createDataDirectory();
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private void createDataDirectory() {
        try {
            Path dataPath = Paths.get(DATA_DIRECTORY);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    /**
     * Save a list of objects to a file
     */
    public <T> boolean saveData(List<T> data, String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
                oos.writeObject(data);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load a list of objects from a file
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> loadData(String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            File file = new File(filepath);
            
            if (!file.exists()) {
                return new ArrayList<>();
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
                return (List<T>) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Save a single object to a file
     */
    public <T> boolean saveObject(T object, String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
                oos.writeObject(object);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving object to " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load a single object from a file
     */
    @SuppressWarnings("unchecked")
    public <T> T loadObject(String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            File file = new File(filepath);
            
            if (!file.exists()) {
                return null;
            }
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
                return (T) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading object from " + filename + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if a data file exists
     */
    public boolean fileExists(String filename) {
        String filepath = DATA_DIRECTORY + File.separator + filename;
        return new File(filepath).exists();
    }
    
    /**
     * Delete a data file
     */
    public boolean deleteFile(String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            return new File(filepath).delete();
        } catch (Exception e) {
            System.err.println("Error deleting file " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the size of a data file
     */
    public long getFileSize(String filename) {
        try {
            String filepath = DATA_DIRECTORY + File.separator + filename;
            return new File(filepath).length();
        } catch (Exception e) {
            System.err.println("Error getting file size for " + filename + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * List all data files
     */
    public List<String> listDataFiles() {
        List<String> files = new ArrayList<>();
        try {
            File dataDir = new File(DATA_DIRECTORY);
            if (dataDir.exists() && dataDir.isDirectory()) {
                File[] fileArray = dataDir.listFiles();
                if (fileArray != null) {
                    for (File file : fileArray) {
                        if (file.isFile()) {
                            files.add(file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error listing data files: " + e.getMessage());
        }
        return files;
    }
    
    /**
     * Backup all data files to a backup directory
     */
    public boolean backupData() {
        try {
            String backupDir = DATA_DIRECTORY + "_backup_" + System.currentTimeMillis();
            Path backupPath = Paths.get(backupDir);
            Files.createDirectories(backupPath);
            
            File dataDir = new File(DATA_DIRECTORY);
            if (dataDir.exists() && dataDir.isDirectory()) {
                File[] files = dataDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            Path source = file.toPath();
                            Path target = backupPath.resolve(file.getName());
                            Files.copy(source, target);
                        }
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error backing up data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get data directory path
     */
    public String getDataDirectory() {
        return DATA_DIRECTORY;
    }
}
