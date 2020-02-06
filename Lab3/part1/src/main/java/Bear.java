public class Bear implements Runnable {
    private HoneyPot honeyPot;

    public Bear(HoneyPot honeyPot) {
        this.honeyPot = honeyPot;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){

        }
    }
}
