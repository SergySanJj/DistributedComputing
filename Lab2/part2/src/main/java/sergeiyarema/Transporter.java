package sergeiyarema;


import java.util.*;
import java.util.concurrent.Semaphore;

public class Transporter implements Runnable {
    private static int carryingTime = 100;
    private List<Item> fromStorage;
    private List<Item> truckItems = new LinkedList<>();
    private Semaphore prodSemaphore;

    public Transporter(List<Item> fromStorageItems, Semaphore prodSemaphore) {
        this.fromStorage = fromStorageItems;
        this.prodSemaphore = prodSemaphore;
    }

    public List<Item> getTruckItems() {
        return truckItems;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (fromStorage) {
                    while (fromStorage.isEmpty()) {
                        fromStorage.wait();
                    }
                }
                Item someItem;
                synchronized (fromStorage) {
                    someItem = fromStorage.remove(0);
                }
                prodSemaphore.release();


                System.out.println("Petrov take item from Ivanov (" + someItem.getId() + ")");
                Thread.sleep(carryingTime);

                synchronized (truckItems) {
                    Thread.sleep(carryingTime);
                    truckItems.add(someItem);
                    System.out.println("Petrov put item (" + someItem.getId() + ")");
                    truckItems.notify();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
