package com.sergeiyarema;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Runner {
    private int n;
    private ThreadPoolExecutor executor;
    private Runnable action;

    public Runner(int n, Runnable action) {
        this.n = n;
        this.action = action;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(n);
    }

    public void startAll() {
        executor.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                action.run();
            }
        });
    }
}
