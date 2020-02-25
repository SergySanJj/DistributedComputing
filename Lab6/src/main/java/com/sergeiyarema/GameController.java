package com.sergeiyarema;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GameController implements Runnable {
    private GameVisualisation gameVisualisation;
    private GameModel gameModel;
    private ReentrantReadWriteLock locker;

    int timeSleep = 50;

    GameController(GameVisualisation gameVisualisation, GameModel gameModel, ReentrantReadWriteLock locker) {
        this.gameVisualisation = gameVisualisation;
        this.gameModel = gameModel;
        this.locker = locker;
    }

    @Override
    public void run() {
        locker.writeLock().lock();
        gameModel.swapField();
        gameVisualisation.repaint();
        locker.writeLock().unlock();
        try {
            Thread.sleep(timeSleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

