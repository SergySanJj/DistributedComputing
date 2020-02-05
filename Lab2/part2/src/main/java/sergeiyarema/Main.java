package sergeiyarema;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        Producer producer = new Producer(100);
        Consumer consumer = new Consumer();
        Transit transit = new Transit(producer, consumer);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        executor.execute(producer);
        executor.execute(consumer);
        executor.execute(transit);
        executor.shutdown();
    }

}
