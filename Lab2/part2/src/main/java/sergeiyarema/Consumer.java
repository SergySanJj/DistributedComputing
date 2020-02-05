package sergeiyarema;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Consumer implements Runnable {
    private static int cap = 5;
    private Integer itemsTotal = 0;
    private Queue<Item> items = new LinkedList<>();
    private Semaphore semaphore = new Semaphore(1);

    public Consumer() {
    }

    public void take(Item item) {
        try {
            semaphore.acquire(1);
            items.add(item);
            semaphore.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    public void processItem() {
        if (items.isEmpty())
            return;

        Item item = items.remove();
        System.out.println("Necheporuk analyzing loot with id: " + item.getId());
        itemsTotal++;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processItem();
        }
        Thread.currentThread().interrupt();
    }

    public int getTotal() {
        return itemsTotal;
    }
}
