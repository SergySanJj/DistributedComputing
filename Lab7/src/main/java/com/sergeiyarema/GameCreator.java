package com.sergeiyarema;

import java.awt.*;
import javax.swing.*;

public class GameCreator extends JFrame {
    private static final int gameWidth = 1000;
    private static final int gameHeight = 600;

    private GameCreator(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        this.setSize(new Dimension(gameWidth, gameHeight));
        Dimension dim = getToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - gameWidth / 2, dim.height / 2 - gameHeight / 2);

        Game panel = new Game(this);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SwingUtilities.invokeLater(() -> new GameCreator("Duck Hunt"));
        } catch (Exception e) {
            // pass
        }
    }
}
