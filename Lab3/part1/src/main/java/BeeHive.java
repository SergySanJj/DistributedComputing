public class BeeHive implements Runnable {
    private HoneyPot honeyPot;
    private int count;

    public BeeHive(HoneyPot honeyPot, int count) {
        this.honeyPot = honeyPot;
        this.count = count;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){

        }
    }
}
