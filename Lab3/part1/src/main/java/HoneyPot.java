import java.util.concurrent.Semaphore;

public class HoneyPot {
    public static final int capacity = 20;
    private Semaphore semaphore = new Semaphore(1);
    private int currentHoney = 0;

    public int getCurrentHoney() {
        return currentHoney;
    }

    public void kk(){

    }
}
