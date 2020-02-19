package com.sergeiyarema;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        int n = 10;
        Field field = new Field(n);
        Random random = new Random();

        executor.execute(() -> {
            nature(n, field, random);
        });

        executor.execute(() -> {
            gardener(n, field, random);
        });

        executor.execute(() -> {
            printer(field);
        });

        executor.execute(() -> {
            fileSaver(field);
        });
    }

    private static void fileSaver(Field field) {
        File file = new File("garden.txt");
        BufferedWriter pw = null;
        try {
            new FileWriter(file).close();

            while (!Thread.currentThread().isInterrupted()) {
                pw = new BufferedWriter(new FileWriter(file, true));
                pw.write("\n" + field.toString());
                pw.close();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printer(Field field) {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println(field);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void gardener(int n, Field field, Random random) {
        while (!Thread.currentThread().isInterrupted()) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if (!field.getState(x, y)) {
                field.waterPlant(x, y);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void nature(int n, Field field, Random random) {
        while (!Thread.currentThread().isInterrupted()) {
            field.setState(random.nextInt(n), random.nextInt(n), random.nextBoolean());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
