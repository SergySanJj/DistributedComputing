package com.sergeiyarema;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CivilizationRunner extends Thread {
    private LifeModel lifeModel;
    private CyclicBarrier barrier;
    private ReentrantReadWriteLock locker;
    private int type;

    CivilizationRunner(LifeModel lifeModel, CyclicBarrier barrier, ReentrantReadWriteLock locker, int type) {
        this.lifeModel = lifeModel;
        this.barrier = barrier;
        this.locker = locker;
        this.type = type;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            locker.readLock().lock();
            lifeModel.simulate(type);
            locker.readLock().unlock();
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

