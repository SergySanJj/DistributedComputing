package com.sergeiyarema;

import java.util.concurrent.Semaphore;

public class HoneyPot {
    public static final int capacity = 20;
    private Semaphore semaphore = new Semaphore(1);
    private int currentHoney = 0;
    private final Object mutex = new Object();

    public void subscribe(Statement waitIfStatement, Action action, Statement wakeUpCondition) {
        try {
            synchronized (mutex) {
                while (Boolean.TRUE.equals(waitIfStatement.isTrue())) {
                    mutex.wait();
                }
                action.act();
                if (Boolean.TRUE.equals(wakeUpCondition.isTrue())) {
                    mutex.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isFull() {
        return currentHoney == capacity;
    }


    public void addHoney() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (currentHoney < capacity) {
            currentHoney++;
        }
        semaphore.release();
    }

    public void addHoney(String whoAdded){
        addHoney();
        System.out.println(whoAdded);
    }

    public void eatAllHoney() {
        try {
            semaphore.acquire();
            System.out.println("All honey eaten");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        currentHoney = 0;

        semaphore.release();
    }
}
