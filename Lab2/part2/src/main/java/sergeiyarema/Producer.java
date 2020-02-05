package sergeiyarema;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

public class Producer implements Runnable {
    private static int cap = 15;
    private Queue<Item> items = new LinkedList<>();
    private Semaphore semaphore = new Semaphore(1);
    private int N;

    public Producer(int N) {
        this.N = N;
    }

    private void produce() {
        try {
            if (items.size() >= cap)
                return;
            semaphore.acquire();
            Item item = new Item();
            System.out.println("Ivanov found new item " + item.getId());
            items.add(item);
            semaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Item get() {
        if (items.isEmpty())
            return null;
        try {
            semaphore.acquire();
            Item item = items.remove();
            semaphore.release();
            return item;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void run() {
        while (N > 0) {
            produce();
            N--;
        }
    }
}
