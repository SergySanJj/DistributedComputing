package com.sergeiyarema;

public class ReadWriteLock {
    private int readersCount = 0;
    private boolean writing = false;
    private Thread writerThread;

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
        writerThread = Thread.currentThread();
    }

    public synchronized void writeUnlock() {
        if (!writing || writerThread != Thread.currentThread())
            throw new IllegalMonitorStateException();
        writing = false;
        writerThread = null;
        notifyAll();
    }
}