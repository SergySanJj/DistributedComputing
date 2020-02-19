package com.sergeiyarema;

import java.io.*;

public class Database {
    private String databaseName;

    private Database(String databaseName) throws IOException {
        this.databaseName = databaseName;
        creatDatabaseFile();
    }

    private void creatDatabaseFile() {
        try {
            File file = new File(databaseName + ".txt");
            if (file.createNewFile()) {
                System.out.println("Database created: " + databaseName);
            } else {
                System.out.println("Database already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDatabaseFileName() {
        return databaseName + ".txt";
    }

    public void dropDatabase() throws IOException {
        new FileWriter(databaseName + ".txt");
    }

    public FileWriter getWriteHandler() throws IOException {
        return new FileWriter(databaseName + ".txt", true);
    }

    public FileReader getReadHandler() throws FileNotFoundException {
        return new FileReader(databaseName + ".txt");
    }


    public static Database create(String databaseName) throws IOException {
        return new Database(databaseName);
    }
}
