package com.sergeiyarema;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Field {
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    private Plant[][] field;

    public Field(int n) {
        field = new Plant[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                field[i][j] = new Plant();
            }
        }
    }

    public void setState(int x, int y, boolean state) {
        writeLock.lock();
        field[y][x].setState(state);
        writeLock.unlock();
    }

    public boolean getState(int x, int y) {
        return field[y][x].getState();
    }

    public void waterPlant(int x, int y) {
        setState(x, y, true);
    }

    @Override
    public String toString() {
        readLock.lock();
        StringBuilder res = new StringBuilder();
        for (Plant[] plantsRow : field) {
            for (Plant plant : plantsRow) {
                res.append(plant.toString()).append("\t");
            }
            res.append("\n");
        }
        readLock.unlock();
        return res.toString();
    }
}
