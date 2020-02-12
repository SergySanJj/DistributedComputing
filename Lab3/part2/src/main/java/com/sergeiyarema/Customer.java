package com.sergeiyarema;

import java.util.concurrent.ThreadPoolExecutor;

public class Customer implements Runnable {
    private static int maxId = 0;
    private int id;
    private final Object sleepObject = new Object();
    private Barbershop barbershop;
    private Boolean shaved = false;

    public Customer(Barbershop barbershop) {
        this.barbershop = barbershop;
        id = maxId;
        maxId++;
    }

    @Override
    public void run() {
        System.out.println("New Customer " + id);
//        if (!barbershop.isBarberWorking())
//            barbershop.getBarberNotifier().notify();
        synchronized (shaved) {
            barbershop.trySitInChair(this);
        }
        while (!shaved) {
            try {
                sleepObject.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shave() {
        System.out.println("Shaved " + id);
        synchronized (shaved) {
            shaved = true;
            sleepObject.notify();
        }
    }

    public void wakeUp() {
        sleepObject.notify();
    }

}
