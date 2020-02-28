package com.sergeiyarema;

import com.sergeiyarema.assets.Textures;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HunterKeyListener implements KeyListener {
    private Game game;
    private ExecutorService bulletExecutor;

    HunterKeyListener(Game newPanel) {
        game = newPanel;
        bulletExecutor = Executors.newFixedThreadPool(game.getMaxBullets());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (game != null) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
                case KeyEvent.VK_SPACE:
                    if (game.hunter.getCountBullet() < game.getMaxBullets()) {
                        bulletExecutor.execute(
                                new Bullet(game, game.hunter,
                                        game.hunter.getX() + Hunter.getSizeX() / 2,
                                        game.hunter.getY()));

                    }
                    break;
                case KeyEvent.VK_LEFT:
                    game.hunter.setKeys(true, false);
                    if (game.hunter.getX() > 0 && game.hunter.getSide() != 1) {
                        game.hunter.setIcon(new ImageIcon(Textures.LHUNTER));
                        game.hunter.setSide(1);

                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    game.hunter.setKeys(false, true);
                    if (game.hunter.getX() < game.getWidth() - Hunter.getSizeX() && game.hunter.getSide() != 2) {
                        game.hunter.setIcon(new ImageIcon(Textures.RHUNTER));
                        game.hunter.setSide(2);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT || keyEvent.getKeyCode() == KeyEvent.VK_LEFT)
            game.hunter.setKeys(false, false);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { // ignore
    }
}
