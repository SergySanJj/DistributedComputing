package sergeiyarema;


import java.util.*;

public class Transporter implements Runnable {
    private static int carryingTime = 100;
    private List<Item> fromStorage;
    private List<Item> truckItems = new LinkedList<>();

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
                    Item someItem = fromStorage.remove(0);

                    System.out.println("Petrov take item from Ivanov (" + someItem.getId() + ")");
                    Thread.sleep(carryingTime);

                    synchronized (truckItems) {
                        Thread.sleep(carryingTime);
                        truckItems.add(someItem);
                        System.out.println("Petrov put item (" + someItem.getId() + ")");
                        truckItems.notify();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
