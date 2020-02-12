package com.sergeiyarema;

import java.util.concurrent.*;

public class CustomerGenerator implements Runnable {
    private Barbershop barbershop;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public CustomerGenerator(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(
                        ThreadLocalRandom.current().
                                nextInt(Config.Customer.generationInterval, Config.Customer.generationSigma));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executorService.submit(new Customer(barbershop));
        }

        try {
            executorService.shutdownNow();
            executorService.awaitTermination(0, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
