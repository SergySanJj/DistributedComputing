package com.sergeiyarema;

public class Custumer implements Runnable {
    private static int maxId = 0;
    private int id;
    private final Object sleepObject = new Object();

    public Custumer() {
        id = maxId;
        maxId++;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

        }
    }

    public void wakeUp() {
        sleepObject.notify();
    }

}
