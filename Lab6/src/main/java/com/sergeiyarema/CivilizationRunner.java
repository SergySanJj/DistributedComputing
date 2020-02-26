package com.sergeiyarema;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CivilizationRunner extends Thread {
    private GameModel gameModel;
    private CyclicBarrier barrier;
    private int type;

    CivilizationRunner(GameModel gameModel, CyclicBarrier barrier, int type) {
        this.gameModel = gameModel;
        this.barrier = barrier;
        this.type = type;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                gameModel.simulate(type);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

