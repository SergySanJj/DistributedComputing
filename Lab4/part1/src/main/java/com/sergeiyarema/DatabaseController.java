package com.sergeiyarema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private Database database;
    private ReadWriteLock readWriteLock = new ReadWriteLock();

    public DatabaseController(Database database) {
        this.database = database;
    }

    public List<String> getPhoneNumbers(String username) {
        try {
            readWriteLock.readLock();

            List<String> res = new ArrayList<>();
            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                if (parseRow(line, 0).equals(username))
                    res.add(parseRow(line, 1));
                line = fileReader.readLine();
            }
            System.out.println("[get] Found: " + res.toString() + " for user: " + username);
            return res;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readUnlock();
        }
        System.out.println("[get] Found no phone number for: " + username);
        return null;
    }

    public String getUsername(String phoneNumber) {
        try {
            readWriteLock.readLock();

            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                if (parseRow(line, 1).equals(phoneNumber)) {
                    String user = parseRow(line, 0);
                    System.out.println("[get] Found user: " + user + " by phone: " + phoneNumber);
                    return user;
                }
                line = fileReader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readUnlock();
        }
        System.out.println("[get] Found no user with phone: " + phoneNumber);
        return null;
    }

    public void printAll() {
        try {
            readWriteLock.readLock();
            System.out.println("Printing: ");
            BufferedReader fileReader = new BufferedReader(database.getReadHandler());
            String line = fileReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = fileReader.readLine();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readUnlock();
        }
    }

    public void addRecord(String username, String phoneNumber) {
        PrintWriter pw = null;
        try {
            readWriteLock.writeLock();
            pw = new PrintWriter(new BufferedWriter(database.getWriteHandler()));
            pw.println(username + " " + phoneNumber);
            System.out.println("[add] Adding: " + username + " " + phoneNumber);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            pw.close();
            readWriteLock.writeUnlock();
        }
    }

    public void deleteRecord(String username, String phoneNumber) {
        try {
            readWriteLock.writeLock();

            BufferedReader reader = new BufferedReader(database.getReadHandler());
            String curr;
            String remove = username + " " + phoneNumber;
            int cnt = 0;
            while ((curr = reader.readLine()) != null) {
                String trimmedLine = curr.trim();
                if (trimmedLine.equals(remove)) break;
                cnt++;
            }
            reader.close();
            if (curr != null) {
                System.out.println("[del] Removing: " + username + " " + phoneNumber);
                database.removeLines(cnt, 1);
            } else System.out.println("[del] Found no user: " + username + " " + phoneNumber);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeUnlock();
        }
    }


    private static String parseRow(String line, int columnNumber) {
        return line.split("\\s+")[columnNumber];
    }
}

