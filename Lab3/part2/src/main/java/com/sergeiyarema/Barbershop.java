package com.sergeiyarema;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barbershop {
    private AtomicInteger spaces = new AtomicInteger(15);
    private final Semaphore barbers = new Semaphore(1, true);
    private final Semaphore customers = new Semaphore(0, true);

    public void callBarber(){
        try {
            barbers.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void freeBarber() {
        barbers.release();
    }

    public void freeCustomerQueue(){
        customers.release();
    }

    public void pollCustomer() {
        try {
            customers.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getSpace(){
        return spaces.get();
    }
    public void decSpace(){
        spaces.decrementAndGet();
    }

    public void incSpace(){
        spaces.incrementAndGet();
    }

    public boolean hasWaitingCustomers(){
        return barbers.hasQueuedThreads();
    }
}
