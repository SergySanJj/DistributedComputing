package sergeiyarema;

import java.util.*;

public class Producer implements Runnable {
    private static int prodTime = 10;
    private List<Item> fromStorageItems = new LinkedList<>();

    public List<Item> getFromStorageItems() {
        return fromStorageItems;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Item someItem = getStuff();
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
            System.out.println("Ivanov see no more items at storage");
        }
    }

    private Item getStuff() {
        return new Item();
    }
}
