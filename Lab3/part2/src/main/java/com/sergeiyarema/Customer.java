package com.sergeiyarema;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private static int maxID = 0;

    private int id;
    private Barbershop barbershop;

    public Customer(Barbershop barbershop) {
        id = maxID;
        maxID++;
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        System.out.println("Customer " + id + " walks in");
        if (barbershop.getSpace() < 0) {
            System.out.println("Customer " + id + " walks out");
        }
        barbershop.freeCustomerQueue();
        if (barbershop.hasWaitingCustomers()) {
            barbershop.decSpace();
            System.out.println("Customer " + id + " waits");
            barbershop.callBarber();
            barbershop.incSpace();
        } else {
            barbershop.callBarber();
        }
    }
}