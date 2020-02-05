package sergeiyarema;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Transit implements Runnable {
    private final Object mutex = new Object();
    private Producer producer;
    private Consumer consumer;
    private Semaphore semaphore = new Semaphore(1);
    private Item item = null;


    public Transit(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    public void tryGet() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        item = producer.get();
    }

    public void send() {
        consumer.take(item);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            tryGet();
            if (item != null) {
                System.out.println("Petrov carrying item " + item.getId());
                send();
                System.out.println("Petrov sent item " + item.getId());
            } else {
                semaphore.release();
            }
        }
    }
}
