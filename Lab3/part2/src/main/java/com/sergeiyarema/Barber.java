package com.sergeiyarema;

public class Barber implements Runnable {
    private Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Customer customer = barbershop.pollCustomer();
            if (customer == null)
                continue;
            try {
                customer.getHaircut();
                countCustomerCheck(customer);
                barbershop.freeBarber();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void countCustomerCheck(Customer customer) {
        System.out.println("Customer " + customer.getId() + " pays and leaves");
        barbershop.freeChair();
    }
}

