package com.sergeiyarema;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameUpdater extends Thread {
    private static final int timeBetweenDucks = 1000;
    private Game game;
    private ExecutorService duckExecutor = Executors.newFixedThreadPool(4);
    private ExecutorService hunterExecutor = Executors.newFixedThreadPool(1);

    GameUpdater(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        hunterExecutor.execute(game.hunter());

        while (!Thread.currentThread().isInterrupted()) {
            if (game.ducksNeeded()) {
                Duck duck = new Duck(game.getWidth(), game.getHeight(), game);
                game.addDuck(duck);
                duckExecutor.submit(duck);
            }
            try {
                sleep(timeBetweenDucks);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
