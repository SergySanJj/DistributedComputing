package com.sergeiyarema;

class Semaphore {
    private int maxValue;
    private int value;
    private final Object lock = new Object();

    public Semaphore(int maxValue) {
        this.maxValue = maxValue;
    }

    public void enter() {
        synchronized (lock) {
            value++;
            while (value > maxValue) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void leave() {
        synchronized (lock) {
            if (value > 0)
                value--;
            lock.notifyAll();
        }

    }
}