package com.sergeiyarema;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Hive {
    private HoneyPot honeyPot;
    private int beeCount;
    private ThreadPoolExecutor executor;


    public Hive(int beeCount, HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
        this.beeCount = beeCount;

        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(beeCount);
    }

    public void startHiveWorkers() {
        for (int i = 0; i < beeCount; i++) {
            executor.execute(new Bee(honeyPot));
        }
    }

    public void stopHiveWorkers() {
        executor.shutdownNow();
        try {
            executor.awaitTermination(0, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
