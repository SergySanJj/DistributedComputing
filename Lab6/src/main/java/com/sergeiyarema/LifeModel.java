package com.sergeiyarema;

class LifeModel {
    private static int[][] neighbors =
            new int[][]{{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};


    private int[][] mainField;
    private int[][] backField;
    private int width;
    private int height;


    LifeModel(int width, int height) {
        this.width = width;
        this.height = height;
        mainField = new int[height][width];
        backField = new int[height][width];
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    void clear() {
        for (int i = 0; i < height; i++) for (int j = 0; j < width; j++) mainField[i][j] = 0;
    }

    void setCell(int x, int y, int c) {
        mainField[x][y] = c;
    }

    int getCell(int x, int y) {
        return mainField[x][y];
    }

    void simulate(int type) {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                int n = countNeighbors(x, y, type);
                backField[x][y] = simulateCell(mainField[x][y], n, type);
            }
        }
    }

    void swapField() {
        int[][] t = mainField;
        mainField = backField;
        backField = t;
    }

    private byte countNeighbors(int xA, int yA, int type) {
        byte n = 0;
        for (int i = 0; i < 8; i++) {
            int x = xA + neighbors[i][0];
            int y = yA + neighbors[i][1];
            if (x >= 0 && x < height && y >= 0 && y < width) {
                if (mainField[x][y] == type) n++;
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

