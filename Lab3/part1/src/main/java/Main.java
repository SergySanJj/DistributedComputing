import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    static void main(String[] args) {
        int beeCount = 10;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        HoneyPot honeyPot = new HoneyPot();
        Bear bear = new Bear(honeyPot);
        BeeHive beeHive = new BeeHive(honeyPot, beeCount);

        executor.execute(bear);
        executor.execute(beeHive);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
    }
}
