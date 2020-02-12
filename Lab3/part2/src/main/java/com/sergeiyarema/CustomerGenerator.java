package com.sergeiyarema;

import java.util.concurrent.*;

public class CustomerGenerator implements Runnable {
    private Barbershop barbershop;
    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    public CustomerGenerator(Barbershop barbershop) {
        this.barbershop = barbershop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            generationWait();
            executorService.submit(new Customer(barbershop));
        }
        shutdownExecutor();
    }

    private void shutdownExecutor() {
        try {
            executorService.shutdownNow();
            executorService.awaitTermination(0, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void generationWait() {
        try {
            Thread.sleep(
                    ThreadLocalRandom.current().
                            nextInt(Config.Customer.generationInterval, Config.Customer.generationSigma));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
