package com.sergeiyarema;

import java.util.concurrent.Semaphore;

public class HoneyPot {
    public static final int capacity = 20;
    private Semaphore semaphore = new Semaphore(1);
    private int currentHoney = 0;
    private final Object mutex = new Object();

    public int getCurrentHoney() {
        synchronized (mutex) {
            return currentHoney;
        }
    }

    public boolean isFull() {
        synchronized (mutex) {
            return currentHoney == capacity;
        }
    }

    public void createOneHoneyPoint() {
        synchronized (mutex) {
            if (currentHoney < capacity)
                currentHoney++;
        }
    }

    public void eatAll() {
        synchronized (mutex) {
            currentHoney = 0;
        }
    }

    public void runAction(Runnable action) {
        try {
            semaphore.acquire();
            action.run();
            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
