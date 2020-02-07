package com.sergeiyarema;

public class Bear implements Runnable {
    private HoneyPot honeyPot;

    public Bear(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            honeyPot.runAction(() -> {
                System.out.println("Bear awakes");
                System.out.println("Bear eats " + honeyPot.getCurrentHoney() + " honey");
                honeyPot.eatAll();
            });
        }
    }
}
