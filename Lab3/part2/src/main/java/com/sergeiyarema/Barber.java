package com.sergeiyarema;

public class Barber implements Runnable {
    private Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            barbershop.pollCustomer(
                    (customer) -> {
                        customer.getHaircut();
                        countCustomerCheck(customer);
                    }
            );
        }
    }

    private void countCustomerCheck(Customer customer) {
        System.out.println("Customer " + customer.getId() + " pays and leaves");
        barbershop.freeChair();
    }
}

