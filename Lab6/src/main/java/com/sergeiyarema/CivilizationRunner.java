package com.sergeiyarema;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CivilizationRunner extends Thread {
    private GameModel gameModel;
    private CyclicBarrier barrier;
    private ReentrantReadWriteLock locker;
    private int type;

    CivilizationRunner(GameModel gameModel, CyclicBarrier barrier, ReentrantReadWriteLock locker, int type) {
        this.gameModel = gameModel;
        this.barrier = barrier;
        this.locker = locker;
        this.type = type;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            locker.writeLock().lock();
            gameModel.simulate(type);
            locker.writeLock().unlock();
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

