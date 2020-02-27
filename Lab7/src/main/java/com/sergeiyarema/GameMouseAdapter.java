package com.sergeiyarema;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter {
    private Game panel;
    private static final int maxRadius = 100;

    GameMouseAdapter(Game newPanel) {
        panel = newPanel;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        for (Duck duck : panel.ducks) {

            if (duck.isShot(x, y))
                duck.kill();
        }
    }
}
