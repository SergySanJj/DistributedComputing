package com.sergeiyarema;

import javax.management.timer.Timer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {
    public static void main(String[] args) throws IOException {
        Database db = Database.create("test");
        db.dropDatabase();
        DatabaseController dbController = new DatabaseController(db);

        Runner readUsername = new Runner(2, () -> {
            dbController.getUsername("1");
            waitFor(100);
        });
        readUsername.startAll();
        Runner readPhone = new Runner(2, () -> {
            dbController.getPhoneNumbers("a");
            waitFor(100);
        });
        readPhone.startAll();
        Runner addUser = new Runner(2, () -> {
            dbController.addRecord("a", "1");
            waitFor(200);
        });
        addUser.startAll();
    }

    private static void waitFor(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
