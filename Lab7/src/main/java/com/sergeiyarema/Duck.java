package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Duck implements Runnable {
    private final int sizeX = 70;
    private final int sizeY = 60;

    private static Random random = new Random();

    private int x;
    private int y;

    private int speedX;
    private int speedY;
    private int width;
    private int duckType;
    private Boolean alive = true;

    private JLabel duck;
    private Game game;

    private int turnRate = Math.abs(random.nextInt()) % 2000 + 1000;
    private long lastTurned;

    public Duck(int newWidth, int newHeight, Game game) {
        width = newWidth;
        int height = newHeight - game.getHeight() * 5 / 12;
        this.game = game;

        duckType = Math.abs(random.nextInt(2));

        if (duckType == 0)
            duck = new JLabel(new ImageIcon(Textures.RDUCK));
        else
            duck = new JLabel(new ImageIcon(Textures.LDUCK));

        duck.setSize(new Dimension(sizeX, sizeY));

        speedX = Math.abs(random.nextInt(5)) + 1;
        speedY = -Math.abs(random.nextInt(4)) - 1;
        if (duckType == 1) speedX = -speedX;

        y = height;
        x = Math.abs(random.nextInt()) % (width - 2*width / 10) + 2*width / 10;
    }

    public void kill() {
        synchronized (alive) {
            alive = false;
        }
    }

    @Override
    public void run() {
        game.add(duck);
        lastTurned = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted() && alive) {
            int nx = x + speedX;
            int ny = y + speedY;
            speedUpdater();
            updateAliveState(nx, ny);

            x = nx;
            y = ny;
            duck.setLocation(x, y);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.remove(duck);
        game.repaint();
        game.ducks.remove(this);
    }

    private void speedUpdater() {
        if (System.currentTimeMillis() - lastTurned >= turnRate) {
            lastTurned = System.currentTimeMillis();
            reverseSpeed();
        }
    }

    private void reverseSpeed() {
        speedX = -speedX;
        if (duckType == 0) {
            duckType = 1;
            duck.setIcon(new ImageIcon(Textures.LDUCK));
        } else {
            duckType = 0;
            duck.setIcon(new ImageIcon(Textures.RDUCK));
        }
    }

    private void updateAliveState(int nx, int ny) {
        synchronized (alive) {
            if (speedX > 0 && nx > width) alive = false;
            else if (speedX < 0 && nx < -sizeX) alive = false;
            if (ny < -sizeY) alive = false;
        }
    }

    public boolean isShot(int posX, int posY) {
        return this.x < posX && posX < this.x + this.sizeX && this.y < posY && posY < this.y + this.sizeY;
    }
}
