package com.sergeiyarema;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

public class Barber implements Runnable {
    private BarbershopQueue queue = new BarbershopQueue();
    private final Object sleep = new Object();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (queue.isEmpty()) {
                try {
                    System.out.println("Barber starts to sleep");
                    sleep.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }


        }
    }

    public void wakeUp(){
        sleep.notify();
    }
}

