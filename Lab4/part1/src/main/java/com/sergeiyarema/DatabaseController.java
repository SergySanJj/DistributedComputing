package com.sergeiyarema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DatabaseController {
    private Database database;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public DatabaseController(Database database) {
        this.database = database;
    }

    public List<String> getPhoneNumbers(String username) {
        List<String> res = new ArrayList<>();
        try {
            readLock.lock();

            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                if (parseRow(line, 0).equals(username))
                    res.add(parseRow(line, 1));
                line = fileReader.readLine();
            }
            System.out.println("Found: " + res.toString() + " for user: " + username);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("Found no phone number for: " + username);
        return null;
    }

    public String getUsername(String phoneNumber) {
        BufferedReader fileReader = null;
        try {
            readLock.lock();

            fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                if (parseRow(line, 1).equals(phoneNumber)) {
                    String user = parseRow(line, 0);
                    System.out.println("Found user: " + user + " by phone: " + phoneNumber);
                    return user;
                }
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("Found no user with phone: " + phoneNumber);
        return null;
    }

    public void printAll() {
        try {
            readLock.lock();
            System.out.println("Printing: ");
            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = fileReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void addRecord(String username, String phoneNumber) {
        PrintWriter pw = null;
        try {
            writeLock.lock();
            pw = database.getWriteHandler();
            pw.println(username + " " + phoneNumber);
            System.out.println("Adding: " + username + " " + phoneNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
            writeLock.unlock();
        }
    }

    private static String parseRow(String line, int columnNumber) {
        return line.split("\\s+")[columnNumber];
    }
}

