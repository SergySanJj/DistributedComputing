package com.sergeiyarema;

class Semaphore {
    private int maxValue;
    private int value = 0;
    private final Object lock = new Object();

    public Semaphore(int maxValue) {
        this.maxValue = maxValue;
    }

    public void acquire() {
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

    public void release() {
        synchronized (lock) {
            if (value > 0)
                value--;
            lock.notifyAll();
        }

    }
}