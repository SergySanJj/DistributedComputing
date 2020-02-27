package com.sergeiyarema;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter {
    private Game game;

    GameMouseAdapter(Game newPanel) {
        game = newPanel;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (Duck duck : game.ducks) {
            if (duck.isShot(x, y))
                duck.kill();
        }
    }
}
