package com.sergeiyarema;

public class ReadWriteLock {
    private int readersCount = 0;
    private boolean writing = false;

    public synchronized void readLock() throws InterruptedException {
        while (writing) wait();
        readersCount++;
    }

    public synchronized void readUnlock() {
        if (readersCount <= 0) throw new IllegalMonitorStateException();
        readersCount--;
        if (readersCount == 0) notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        while (readersCount != 0) wait();
        writing = true;
    }

    public synchronized void writeUnlock() {
        if (!writing)
            return;
        writing = false;
        notifyAll();
    }
}
