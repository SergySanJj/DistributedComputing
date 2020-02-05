package sergeiyarema;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        Producer producer = new Producer();
        Transporter transporter = new Transporter(producer.getFromStorageItems());
        Consumer consumer = new Consumer(transporter.getTruckItems());

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

        executor.execute(transporter);
        executor.execute(producer);
        executor.execute(consumer);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdownNow();
    }
}
