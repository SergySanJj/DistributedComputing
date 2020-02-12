package com.sergeiyarema;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barbershop {
    private AtomicInteger queueSpace = new AtomicInteger(Config.Barbershop.queueCapacity);
    private final Semaphore barbers = new Semaphore(1, true);
    private final Semaphore customers = new Semaphore(1, true);
    private final Object chairMutex = new Object();

    private Customer currentCustomer;

    public void handleNewCustomer(Customer customer,
                                  Action onEnter,
                                  Action onWait,
                                  Action onSit,
                                  Action onDecline) {

        onEnter.act();
        if (getSpace() <= 0) {
            onDecline.act();
            return;
        }
        standInQueue();

        if (hasWaitingCustomers()) {
            decSpace();
            onWait.act();
            sitInChair(customer, onSit);
            incSpace();
        } else {
            sitInChair(customer, onSit);
        }

    }

    public void freeBarber() {
        barbers.release();
    }

    public void standInQueue() {
        try {
            synchronized (chairMutex) {
                customers.acquire();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void pollCustomer(ActionWithCustomer action) {
        customers.release();
        if (currentCustomer == null)
            return;
        action.act(currentCustomer);
        freeBarber();
    }

    public int getSpace() {
        synchronized (chairMutex) {
            return queueSpace.get();
        }
    }

    public void decSpace() {
        synchronized (chairMutex) {
            queueSpace.decrementAndGet();
        }
    }

    public void incSpace() {
        synchronized (chairMutex) {
            queueSpace.incrementAndGet();
        }
    }

    public boolean hasWaitingCustomers() {
        synchronized (chairMutex) {
            return barbers.hasQueuedThreads();
        }
    }

    public void sitInChair(Customer customer, Action onSit) {
        try {
            barbers.acquire();
            synchronized (chairMutex) {
                currentCustomer = customer;
            }
            onSit.act();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void freeChair() {
        synchronized (chairMutex) {
            currentCustomer = null;
        }
    }
}
