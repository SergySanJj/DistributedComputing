package com.sergeiyarema;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Updater implements Runnable {
    private LifePanel lifePanel;
    private LifeModel lifeModel;
    private ReentrantReadWriteLock locker;

    int timeSleep = 50;
    private int first;

    Updater(LifePanel lifePanel, LifeModel lifeModel, ReentrantReadWriteLock locker) {
        this.lifePanel = lifePanel;
        this.lifeModel = lifeModel;
        this.locker = locker;
        first = 1;
    }

    @Override
    public void run() {
        if (first == 1) first = 0;
        else {
            locker.writeLock().lock();
            lifeModel.swapField();
            locker.writeLock().unlock();
            lifePanel.repaint();
            try {
                Thread.sleep(timeSleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

