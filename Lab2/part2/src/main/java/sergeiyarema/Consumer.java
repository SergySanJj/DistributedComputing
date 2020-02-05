package sergeiyarema;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {
    private static int countingTime = 1000;
    private static  int cap = 6;
    private List<Item> truckItems;
    private int total = 0;
    private Semaphore semaphore = new Semaphore(cap);

    public Consumer(List<Item> truckItems) {
        this.truckItems = truckItems;
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (truckItems) {
                    while (truckItems.isEmpty()) {
                        truckItems.wait();
                    }

                    Item someItem = truckItems.remove(0);
                    total += someItem.getPrice();

                    System.out.println("Necheporchuk counting (" + someItem.getId() + ")");
                    Thread.sleep(countingTime);
                    semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Necheporchuk Total:  " + total);
        }
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
