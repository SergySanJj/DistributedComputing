package com.sergeiyarema;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barbershop {
    private AtomicInteger queueSpace = new AtomicInteger(Config.Barbershop.queueCapacity);
    private final Semaphore barbers = new Semaphore(1, true);
    private final Semaphore customers = new Semaphore(0, true);
    private final Object chairMutex = new Object();

    private Customer currentCustomer;


    public Object chairMutex() {
        return chairMutex;
    }

    public void freeBarber() {
        barbers.release();
    }

    public void freeCustomerQueue() {
        customers.release();
    }

    public Customer pollCustomer() {
        boolean hasFallenAsleep = false;
        if (!customers.hasQueuedThreads()) {
            System.out.println("Barber see no clients so he sleeps");
            hasFallenAsleep = true;
        }
        try {
            customers.acquire();
            if (hasFallenAsleep) {
                System.out.println("Barber was waken up by Customer " + currentCustomer.getId());
            }
            return currentCustomer;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public int getSpace() {
        return queueSpace.get();
    }

    public void decSpace() {
        queueSpace.decrementAndGet();
    }

    public void incSpace() {
        queueSpace.incrementAndGet();
    }

    public boolean hasWaitingCustomers() {
        return barbers.hasQueuedThreads();
    }

    public void sitInChair(Customer customer) {
        try {
            barbers.acquire();
            currentCustomer = customer;
            System.out.println("Customer " + customer.getId() + " sits");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void freeChair() {
        currentCustomer = null;
    }
}
