package com.sergeiyarema;

import com.sergeiyarema.assets.Textures;

import javax.swing.*;
import java.awt.*;

public class Bullet implements Runnable {
    private int x;
    private int y;

    private static final int flySpeed = 10;
    private static final int sizeX = 100;
    private static final int sizeY = 100;

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
            if (y < 0)
                break;
            y -= flySpeed;

            bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);

            game.runForeachDuckActionIf(
                    duck -> duck.isShot(x, y),
                    duck -> {
                        duck.kill();
                        game.remove(bulletLabel);
                        hunter.addBullet(-1);
                    }
            );

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.remove(bulletLabel);
        hunter.addBullet(-1);
    }
}
