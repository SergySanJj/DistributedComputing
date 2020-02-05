package sergeiyarema;


import java.util.*;
import java.util.concurrent.Semaphore;

public class Transporter implements Runnable {
    private static int carryingTime = 100;
    private List<Item> fromStorage;
    private List<Item> truckItems = new LinkedList<>();

    public void setProdSemaphore(Semaphore prodSemaphore) {
        this.prodSemaphore = prodSemaphore;
    }

    public void setConSemaphore(Semaphore conSemaphore) {
        this.conSemaphore = conSemaphore;
    }

    private Semaphore prodSemaphore;
    private Semaphore conSemaphore;

    public Transporter(List<Item> fromStorageItems) {
        this.fromStorage = fromStorageItems;
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
                    conSemaphore.acquire();
                    System.out.println("Petrov put item (" + someItem.getId() + ")");
                    truckItems.notify();
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
