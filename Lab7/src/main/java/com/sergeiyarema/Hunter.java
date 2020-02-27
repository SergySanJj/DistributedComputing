package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;

public class Hunter implements Runnable {
    private JLabel hunterLabel;
    private Game game;

    private int x;
    private int y;

    private static final int sizeX = 137;
    private static final int sizeY = 150;

    private static final int dx = 20;
    private int width;
    private volatile int countBullet = 0;

    private int side = 1;

    private boolean keyLeft = false;
    private boolean keyRight = false;

    public Hunter(GameWindow gameWindow, Game game) {
        this.width = game.getWidth();
        this.game = game;
        x = game.getWidth() / 2;
        y = game.getHeight() - sizeY - 30;

        hunterLabel = new JLabel(new ImageIcon(Textures.LHUNTER));
        hunterLabel.setSize(new Dimension(sizeX, sizeY));
        hunterLabel.setLocation(x, y);
        hunterLabel.setVisible(true);
        game.add(hunterLabel);

        HunterKeyListener keyListener = new HunterKeyListener(game);
        gameWindow.addKeyListener(keyListener);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            updatePosition();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        game.remove(hunterLabel);
    }

    public static int getSizeX() {
        return sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }

    public synchronized void addBullet(int num) {
        countBullet += num;
    }

    public synchronized int getCountBullet() {
        return countBullet;
    }

    public synchronized int getSide() {
        return side;
    }

    public synchronized void setKeys(boolean newKeyLeft, boolean newKeyRight) {
        keyLeft = newKeyLeft;
        keyRight = newKeyRight;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized void setSide(int newSide) {
        side = newSide;
    }

    public synchronized void setIcon(Icon icon) {
        hunterLabel.setIcon(icon);
    }

    private synchronized void updatePosition() {
        if (keyLeft && x - dx >= 0)
            x -= dx;
        else if (keyRight && x + dx + sizeX <= width)
            x += dx;
        hunterLabel.setLocation(x, y);
    }
}
