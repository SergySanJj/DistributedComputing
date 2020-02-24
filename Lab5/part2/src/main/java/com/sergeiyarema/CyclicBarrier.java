package com.sergeiyarema;

public class CyclicBarrier {
    private int initialParties;
    private volatile int partiesAwait;
    private Runnable event;

    public CyclicBarrier(int parties, Runnable event) {
        initialParties = parties;
        partiesAwait = parties;
        this.event = event;
    }


    public synchronized void await() throws InterruptedException {
        partiesAwait--;
        if (partiesAwait > 0) {
            this.wait();
        } else {
            partiesAwait = initialParties;
            event.run();
            notifyAll();
        }
    }
}
