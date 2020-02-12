package com.sergeiyarema;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String args[]) {
        Barbershop barbershop = new Barbershop();
        CustomerGenerator customerGenerator = new CustomerGenerator(barbershop);
        Barber barber = new Barber(barbershop);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(barber);
        executor.execute(customerGenerator);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            executor.shutdownNow();
            executor.awaitTermination(0, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
