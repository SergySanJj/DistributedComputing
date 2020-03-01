package com.sergeiyarema;

import com.sergeiyarema.assets.Textures;
import com.sergeiyarema.misc.DuckAction;
import com.sergeiyarema.misc.DuckChecker;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class Game extends JPanel {
    private ImageIcon background = new ImageIcon(Textures.BACKGROUND);
    private static final int maxBullets = 5;
    private static final int maxDucks = 4;

    public static int getMaxDucks() {
        return maxDucks;
    }

    private final Queue<Duck> ducks = new LinkedList<>();

    public void runForeachDuckActionIf(DuckChecker duckChecker, DuckAction action) {
        synchronized (ducks) {
            for (Duck duck : ducks) {
                if (duckChecker.check(duck)) {
                    action.run(duck);
                }
            }
        }
    }

    public void addDuck(Duck duck){
        synchronized (ducks){
            ducks.add(duck);
        }
    }

    public boolean ducksNeeded() {
        return ducks.size() < getMaxDucks();
    }

    public void kill(Duck duck) {
        synchronized (ducks) {
            ducks.remove(duck);
        }
    }


    private Hunter hunter;

    public Hunter hunter() {
        return hunter;
    }


    public Game(GameWindow gameWindow) {
        this.setSize(gameWindow.getSize());
        setBackground(Color.WHITE);

        setLayout(null);
        setSize(getWidth(), getHeight());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(Textures.CROSS);
        setCursor(toolkit.createCustomCursor(image, new Point(), "cross"));
        addMouseListener(new ShootMouseAdapter(this));

        GameUpdater gameUpdater = new GameUpdater(this);
        hunter = new Hunter(gameWindow, this);

        gameUpdater.start();
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
