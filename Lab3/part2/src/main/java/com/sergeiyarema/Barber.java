package com.sergeiyarema;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barber implements Runnable {
    private Barbershop barbershop;

    public Barber(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                barbershop.pollCustomer();
                System.out.println("Customer getting hair cut");
                Thread.sleep(1000);
                System.out.println("Customer Pays and leaves");
                barbershop.freeBarber();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

