package com.sergeiyarema;

import javax.management.timer.Timer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class Main {
    private static Random random = new Random();

    public static void main(String[] args) throws IOException {
        Database db = Database.create("test");
        db.dropDatabase();
        DatabaseController dbController = new DatabaseController(db);

        startAllRunners(dbController);
    }

    private static void startAllRunners(DatabaseController dbController) {
        Runner readUsername = new Runner(2, () -> {
            dbController.getUsername(randomPhone());
            waitFor(100);
        });
        readUsername.startAll();
        Runner readPhone = new Runner(2, () -> {
            dbController.getPhoneNumbers(randomUsername());
            waitFor(100);
        });
        readPhone.startAll();
        Runner addUser = new Runner(2, () -> {
            dbController.addRecord(randomUsername(), randomPhone());
            waitFor(200);
        });
        addUser.startAll();
        Runner removeUser = new Runner(2, () -> {
            dbController.deleteRecord(randomUsername(), randomPhone());
            waitFor(100);
        });
        removeUser.startAll();
    }

    private static String randomPhone() {
        return Integer.toString(Math.abs(random.nextInt()) % 10);
    }

    private static String randomUsername() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 1;
        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static void waitFor(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
