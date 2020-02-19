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
    public void removeLines( int startLine, int numLines) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getDatabaseFileName()));

            StringBuilder sb = new StringBuilder("");

            int linenumber = 0;
            String line;

            while ((line = br.readLine()) != null) {
                if (linenumber < startLine || linenumber >= startLine + numLines)
                    sb.append(line).append("\n");
                linenumber++;
            }
            if (startLine + numLines > linenumber)
                System.out.println("End of file reached.");
            br.close();

            FileWriter fw = new FileWriter(new File(getDatabaseFileName()));
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            System.out.println("Something went horribly wrong: " + e.getMessage());
        }
    }
}
