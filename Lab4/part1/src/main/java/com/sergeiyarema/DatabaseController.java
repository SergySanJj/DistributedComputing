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
            System.out.println("[get] Found: " + res.toString() + " for user: " + username);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("[get] Found no phone number for: " + username);
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
                    System.out.println("[get] Found user: " + user + " by phone: " + phoneNumber);
                    return user;
                }
                line = fileReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("[get] Found no user with phone: " + phoneNumber);
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
            System.out.println("[add] Adding: " + username + " " + phoneNumber);
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
                removeLines(database.getDatabaseFileName(), cnt, 1);
            } else System.out.println("[del] Found no user: " + username + " " + phoneNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private static void removeLines(String filename, int startLine, int numLines) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

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

            FileWriter fw = new FileWriter(new File(filename));
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            System.out.println("Something went horribly wrong: " + e.getMessage());
        }
    }

    private static String parseRow(String line, int columnNumber) {
        return line.split("\\s+")[columnNumber];
    }
}

