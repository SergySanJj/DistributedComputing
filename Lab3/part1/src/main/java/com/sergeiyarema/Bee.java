package com.sergeiyarema;

public class Bee implements Runnable {
    private static int maxId = 0;
    private HoneyPot honeyPot;
    private int beeId;

    public Bee(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
        this.beeId = maxId;
        maxId++;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            honeyPot.subscribe(
                    () -> honeyPot.isFull(),
                    () -> honeyPot.addHoney("Bee " + beeId + " adds honey"),
                    () -> honeyPot.isFull()
            );
    }
    }
}
