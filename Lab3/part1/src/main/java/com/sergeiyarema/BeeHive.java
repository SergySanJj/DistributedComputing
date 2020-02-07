package com.sergeiyarema;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BeeHive implements Runnable {
    private HoneyPot honeyPot;
    private int count;
    private ThreadPoolExecutor executor;

    public BeeHive(HoneyPot honeyPot, int count) {
        this.honeyPot = honeyPot;
        this.count = count;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(count);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            honeyPot.runAction(() -> {
                System.out.println("Bees start to work");
                while (!honeyPot.isFull()) {
                    executor.execute(new Bee(honeyPot));
                }
                System.out.println("Bee wakes Bear");
            });
        }
    }
}
