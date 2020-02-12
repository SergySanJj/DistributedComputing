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

    @Override
    public void run() {
        barbershop.handleNewCustomer(this,
                () -> System.out.println("Customer " + id + " walks in"),
                () -> System.out.println("Customer " + id + " waits"),
                () -> System.out.println("Customer " + id + " sits"),
                () -> System.out.println("Customer " + id + " walks out")
        );
    }

    public int getId() {
        return id;
    }

    public void getHaircut() {
        try {
            Thread.sleep(Config.Customer.hairstyleTime);
            System.out.println("Customer " + getId() + " getting haircut");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}