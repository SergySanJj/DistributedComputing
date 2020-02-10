package com.sergeiyarema;

public class Bear implements Runnable {
    private HoneyPot honeyPot;

    public Bear(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            honeyPot.subscribe(
                    () -> !honeyPot.isFull(),
                    () -> honeyPot.eatAllHoney(),
                    () -> true
            );
        }
    }
}
