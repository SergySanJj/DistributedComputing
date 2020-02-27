package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game extends JPanel {
    private ImageIcon background = new ImageIcon(Textures.BACKGROUND);
    private static final int maxBullets = 5;
    private static final int maxDucks = 4;

    public static int getMaxDucks() {
        return maxDucks;
    }

    ConcurrentLinkedQueue<Duck> ducks = new ConcurrentLinkedQueue<>();

    Hunter hunter = null;
    GameWindow gameWindow;

    public Game(GameWindow gameWindow) {
        this.setSize(gameWindow.getSize());
        setBackground(Color.WHITE);
        this.gameWindow = gameWindow;

        setLayout(null);
        setSize(getWidth(), getHeight());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(Textures.CROSS);
        setCursor(toolkit.createCustomCursor(image, new Point(), "cross"));
        addMouseListener(new GameMouseAdapter(this));

        GameUpdater game = new GameUpdater(this);
        game.start();
    }

    synchronized int getMaxBullets() {
        return maxBullets;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(getWidth(), getHeight());
    }
}
