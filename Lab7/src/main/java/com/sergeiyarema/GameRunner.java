package com.sergeiyarema;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameRunner extends Thread {
    private static final int timeBetweenDucks = 1000;
    private Game game;
    private ExecutorService duckExecutor = Executors.newFixedThreadPool(4);

    GameRunner(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (game.hunter == null) {
            game.hunter = new Hunter(game.gameCreator, game);
            game.hunter.start();
        }

        while (!Thread.currentThread().isInterrupted()) {
            if (game.ducks.size() < game.getMaxDucks()) {
                Duck duck = new Duck(game.getWidth(), game.getHeight(), game);
                game.ducks.add(duck);
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
