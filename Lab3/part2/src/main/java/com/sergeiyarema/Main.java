package com.sergeiyarema;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String args[]) {
        Barbershop barbershop = new Barbershop();
        Barber barber = new Barber(barbershop);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(barber);

        for (int i = 0; i < 10; i++) {
            executor.submit(new Customer(barbershop));
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

//        executor.shutdownNow();
//        try {
//            executor.awaitTermination(0, TimeUnit.MICROSECONDS);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}
