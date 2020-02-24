package com.sergeiyarema;

import java.util.concurrent.CyclicBarrier;

public class Captain implements Runnable {
    private Soldier[] soldiers;
    private int start, end;
    private int needToBeTurned;
    private static volatile boolean captainsAligned = false;
    private CyclicBarrier barrier;

    public Captain(Soldier[] soldiers, int start, int end, CyclicBarrier barrier) {
        this.soldiers = soldiers;
        this.start = start;
        this.end = end;
        this.barrier = barrier;
    }

    public static void setAllAligned(boolean alignment) {
        captainsAligned = alignment;
    }

    @Override
    public void run() {
        commandAll();
        needToBeTurned = end - start;
        while (!Thread.currentThread().isInterrupted() && !captainsAligned) {
            needToBeTurned = 0;
            for (int i = start; i <= end; i++) {
                if (!soldiers[i].correctPositioning(findNeighbour(i))) {
                    needToBeTurned++;
                }
            }
            try {
                System.out.println("Captain " + this.hashCode() + " need to be updated " + needToBeTurned);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            barrierWait(); // All soldiers remembered where to turn

            pushUpdate();

            barrierWait(); // All soldiers executed turn if needed
        }
    }

    private void barrierWait() {
        try {
            barrier.await();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getNeedToBeTurned() {
        return needToBeTurned;
    }

    public void pushUpdate() {
        for (int i = start; i <= end; i++) {
            soldiers[i].turnIfNeeded();
        }
    }

    private void commandAll() {
        for (int i = start; i <= end; i++) {
            soldiers[i].command();
        }
    }

    private Soldier findNeighbour(int soldierPosition) {
        int neighbourPosition = soldierPosition + soldiers[soldierPosition].getStride();
        if (neighbourPosition < 0 || neighbourPosition >= soldiers.length)
            return null;
        else return soldiers[neighbourPosition];
    }
}
