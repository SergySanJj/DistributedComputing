package sergeiyarema;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Producer implements Runnable {
    private static int prodTime = 10;
    private static int cap = 5;
    private final Semaphore semaphore= new Semaphore(cap,true);
    private List<Item> fromStorageItems = new LinkedList<>();

    public List<Item> getFromStorageItems() {
        return fromStorageItems;
    }
    public Semaphore getSemaphore(){
        return semaphore;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                semaphore.acquire();
                Item someItem = findItem();
                Thread.sleep(prodTime);
                synchronized (fromStorageItems) {
                    fromStorageItems.add(someItem);
                    System.out.println("Ivanov taking item (" + someItem.getId() + ")");
                    fromStorageItems.notify();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Ivanov has no more time");
        }
    }

    private Item findItem() {
        return new Item();
    }
}
