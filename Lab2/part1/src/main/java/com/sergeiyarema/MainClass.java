package com.sergeiyarema;

import java.lang.ref.SoftReference;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainClass {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int n;
        System.out.println("Field size");
        n = scanner.nextInt();
        int[][] field = new int[n][n];
        System.out.println("Pooh coords (x,y): ");
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        field[y][x] = 1;

//        print(field);
        parallelSearch(field, 3);
    }

    private static void parallelSearch(int[][] matrix, int workersCount) {
        int hiveWorkersCount = 8;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(hiveWorkersCount);
        Semaphore semaphore = new Semaphore(workersCount);
        AtomicBoolean found = new AtomicBoolean(false);

        for (int i = 0; i < matrix.length; i++) {
            if (found.get())
                break;
            semaphore.acquire();
            executor.execute(new Worker(matrix[i], semaphore, found, i));
        }
        executor.shutdown();
    }

    private static void print(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.println();
        }
    }
}
