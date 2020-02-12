package com.sergeiyarema;

import javax.swing.plaf.TableHeaderUI;

public class Barber implements Runnable {
    private Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        Customer customer;
        while (!Thread.currentThread().isInterrupted()) {
            while ((customer = barbershop.getNextCustomer()) == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
//                System.out.println("Barber starts to sleep");
//                try {
//                    barbershop.getBarberNotifier().wait();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
            }
            customer.shave();
            barbershop.freeChair();
        }
    }
}

