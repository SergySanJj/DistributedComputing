package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Game extends JPanel {
    private int width;
    private int height;

    private ImageIcon background = new ImageIcon(Textures.BACKGROUND);
    private static final int maxBullets = 5;
    private final int maxDucks = 4;

    ConcurrentLinkedQueue<Duck> ducks = new ConcurrentLinkedQueue<>();

    Hunter hunter = null;
    GameCreator gameCreator;

    public Game(GameCreator gameCreator) {
        setBackground(Color.WHITE);
        this.gameCreator = gameCreator;
        width = gameCreator.getWidth();
        height = gameCreator.getHeight();

        setLayout(null);
        setSize(width, height);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(Textures.CROSS);
        Cursor c =
                toolkit.createCustomCursor(image, new Point(getX() + 14, getY() + 14), "breech-sight");
        setCursor(c);

        addMouseListener(new GameMouseAdapter(this));

        GameRunner game = new GameRunner(this);
        game.start();
    }

    public int getMaxDucks() {
        return maxDucks;
    }

    synchronized int getMaxBullets() {
        return maxBullets;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 0, 0, width, height, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(width, height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
