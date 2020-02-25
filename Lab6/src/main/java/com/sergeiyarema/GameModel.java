package com.sergeiyarema;

public class GameModel {
    private static int[][] neighbors =
            new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};


    private int[][] visField;
    private int[][] backField;
    private int width;
    private int height;


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

    synchronized void clear() {
        for (int i = 0; i < height; i++) for (int j = 0; j < width; j++) visField[i][j] = 0;
    }

    synchronized void setCell(int x, int y, int c) {
        visField[x][y] = c;
    }

    int getCell(int x, int y) {
        return visField[x][y];
    }

    synchronized void simulate(int type) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int n = countNeighbors(x, y, type);
                backField[x][y] = simulateCell(visField[x][y], n, type);
            }
        }
    }

    synchronized void swapField() {
        int[][] t = visField;
        visField = backField;
        backField = t;
    }

    private synchronized byte countNeighbors(int xA, int yA, int type) {
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

    private int simulateCell(int self, int n, int type) {
        if (self == type && n < 2) return 0;
        if (self == type && (n == 2 || n == 3)) return type;
        if (self == type) return 0;
        if (n == 3) return type;

        return self;
    }
}

