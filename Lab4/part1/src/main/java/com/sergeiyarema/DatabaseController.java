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
        try {
            readLock.lock();

            List<String> res = new ArrayList<>();
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
        try {
            readLock.lock();

            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
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
            pw = new PrintWriter(new BufferedWriter(database.getWriteHandler()));
            pw.println(username + " " + phoneNumber);
            System.out.println("Adding: " + username + " " + phoneNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
            writeLock.unlock();
        }
    }

    public void deleteRecord(String username, String phoneNumber) {
        try {
            writeLock.lock();
            System.out.println("Trying to remove: " + username + " " + phoneNumber);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(database.getReadHandler());
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String curr;
            String remove = username + " " + phoneNumber;
            while ((curr = reader.readLine()) != null) {
                String trimmedLine = curr.trim();
                if (trimmedLine.equals(remove)) continue;
                writer.write(curr + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            boolean delete = (new File(database.getDatabaseFileName())).delete();
            System.out.println("Del: " + delete);
            boolean stat = tempFile.renameTo(new File(database.getDatabaseFileName()));
            System.out.println("Renaming: " + stat);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private static String parseRow(String line, int columnNumber) {
        return line.split("\\s+")[columnNumber];
    }
}

