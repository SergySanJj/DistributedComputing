package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;

public class Hunter extends Thread {
    private JLabel hunterLabel;
    private Game game;

    private int x;
    private int y;

    public static int getSizeX() {
        return sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }

    private static final int sizeX = 137;
    private static final int sizeY = 150;

    private static final int dx = 20;
    private int width;
    private volatile int countBullet = 0;

    private int side = 1;

    private boolean keyLeft = false;
    private boolean keyRight = false;

    Hunter(GameCreator gameCreator, Game game) {
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
        gameCreator.addKeyListener(keyListener);
    }

    synchronized void addBullet(int num) {
        countBullet += num;
    }

    synchronized int getCountBullet() {
        return countBullet;
    }

    synchronized int getSide() {
        return side;
    }

    synchronized void setKeys(boolean newKeyLeft, boolean newKeyRight) {
        keyLeft = newKeyLeft;
        keyRight = newKeyRight;
    }

    synchronized int getX() {
        return x;
    }

    synchronized int getY() {
        return y;
    }

    synchronized void setSide(int newSide) {
        side = newSide;
    }

    synchronized void setIcon(Icon icon) {
        hunterLabel.setIcon(icon);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (keyLeft && x - dx >= 0) x -= dx;
            else if (keyRight && x + dx + sizeX <= width) x += dx;

            hunterLabel.setLocation(x, y);

            try {
                sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.remove(hunterLabel);
    }
}
