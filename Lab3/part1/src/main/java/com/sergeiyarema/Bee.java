package com.sergeiyarema;

public class Bee implements Runnable {
    HoneyPot honeyPot;

    public Bee(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        honeyPot.createOneHoneyPoint();
        System.out.println("Bee creates honey " + honeyPot.getCurrentHoney() + "/" + HoneyPot.capacity);
    }
}
