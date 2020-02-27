package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;

public class Bullet extends Thread {
    private int x;
    private int y;

    private static final int dy = 10;
    private final static int sizeX = 100;
    private final static int sizeY = 100;

    private Game game;
    private JLabel bulletLabel;
    private Hunter hunter;

    Bullet(Game game, Hunter hunter, int x, int y) {
        this.game = game;
        this.hunter = hunter;
        this.x = x;
        this.y = y;
        this.bulletLabel = new JLabel(new ImageIcon(Textures.BULLET));
        bulletLabel.setSize(new Dimension(sizeX, sizeY));
        bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);
    }

    @Override
    public void run() {
        hunter.addBullet(1);
        game.add(bulletLabel);

        while (!Thread.currentThread().isInterrupted()) {
            if (y < 0) break;
            y -= dy;

            bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);

            for (Duck duck : game.ducks) {
                if (duck.isShot(x, y)) {
                    duck.kill();
                    this.interrupt();
                    break;
                }
            }

            try {
                sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.remove(bulletLabel);
        game.repaint();
        hunter.addBullet(-1);
    }
}
