package com.sergeiyarema;

public class Customer implements Runnable {
    private static int maxID = 0;

    private int id;
    private Barbershop barbershop;

    public Customer(Barbershop barbershop) {
        id = maxID;
        maxID++;
        this.barbershop = barbershop;
    }

    public int getId() {
        return id;
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
            barbershop.sitInChair(this);
            barbershop.incSpace();
        } else {
            barbershop.sitInChair(this);
        }
    }

    public void getHaircut() throws InterruptedException {
        Thread.sleep(Config.Customer.hairstyleTime);
        System.out.println("Customer " + getId() + " getting hair cut");
    }
}