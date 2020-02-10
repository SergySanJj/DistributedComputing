package com.sergeiyarema;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int beeCount = 4;
        ThreadPoolExecutor bearExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        HoneyPot honeyPot = new HoneyPot();
        Bear bear = new Bear(honeyPot);
        Hive hive = new Hive(beeCount, honeyPot);

        bearExecutor.execute(bear);
        hive.startHiveWorkers();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            bearExecutor.shutdownNow();
            bearExecutor.awaitTermination(0, TimeUnit.MILLISECONDS);
            hive.stopHiveWorkers();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
