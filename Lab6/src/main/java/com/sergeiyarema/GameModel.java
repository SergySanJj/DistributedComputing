package com.sergeiyarema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameModel {
    private static int[][] neighbors =
            new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};


    private int[][] visField;
    private int[][] backField;
    private int width;
    private int height;

    private final Object fieldMutex = new Object();
    private ExecutorService chunkExecutor = Executors.newFixedThreadPool(4);
    private CyclicBarrier barrier = new CyclicBarrier(4);

    GameModel(int width, int height) {
        this.width = width;
        this.height = height;
        visField = new int[height][width];
        backField = new int[height][width];
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    public void clear() {
        synchronized (fieldMutex) {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    visField[i][j] = 0;
        }
    }

    public void setCell(int x, int y, int c) {
        synchronized (fieldMutex) {
            visField[x][y] = c;
        }
    }

    int getCell(int x, int y) {
        return visField[x][y];
    }

    public void simulate(int type) {
        synchronized (fieldMutex) {
            System.out.println("Simulating " + type);
            List<Callable<Object>> calls = new ArrayList<>();
            calls.add(getChunkSimulator(0, 0, width / 2, height / 2, type));
            calls.add(getChunkSimulator(width / 2, 0, width, height / 2, type));
            calls.add(() -> {
                simulateChunk(0, height / 2, width / 2, height, type);
                return null;
            });
            calls.add(() -> {
                simulateChunk(width / 2, height / 2, width, height, type);
                return null;
            });

            try {
                chunkExecutor.invokeAll(calls);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Callable<Object> getChunkSimulator(int x0, int y0, int x1, int y1, int type) {
        return () -> {
            simulateChunk(x0, y0, x1, y1, type);
            return null;
        };
    }

    private void simulateChunk(int x0, int y0, int x1, int y1, int type) {
        System.out.println("Simulating chunk" + type);
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                int n = countNeighbors(x, y, type);
                backField[x][y] = simulateCell(visField[x][y], n, type);
            }
        }
    }

    public void swapField() {
        synchronized (fieldMutex) {
            int[][] t = visField;
            visField = backField;
            backField = t;
        }
    }

    private int countNeighbors(int xA, int yA, int type) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            int x = xA + neighbors[i][0];
            int y = yA + neighbors[i][1];
            if (x >= 0 && x < height && y >= 0 && y < width) {
                if (visField[x][y] == type) n++;
            }
        }
        return n;
    }

    private static int simulateCell(int self, int n, int type) {
        if (self == type && n < 2) return 0;
        if (self == type && (n == 2 || n == 3)) return type;
        if (self == type) return 0;
        if (n == 3) return type;

        return self;
    }
}

