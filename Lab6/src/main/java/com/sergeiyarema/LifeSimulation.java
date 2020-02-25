package com.sergeiyarema;

import javax.swing.*;
import java.awt.*;

public class LifeSimulation extends JFrame {
    private LifePanel lifePanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton clearFieldButton;

    private static final int panelWidth = 32;
    private static final int panelHeight = 32;
    private static final int cellSize = 20;

    private LifeSimulation(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        lifePanel = new LifePanel();

        lifePanel.initialize(panelWidth, panelHeight, cellSize);
        add(lifePanel);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(true);
        add(toolBar, BorderLayout.NORTH);

        startButton = new JButton("Start");
        toolBar.add(startButton);
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        toolBar.add(stopButton);
        clearFieldButton = new JButton("Clear");
        toolBar.add(clearFieldButton);

        startButton.addActionListener(
                e -> {
                    lifePanel.startSimulation();
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                });

        stopButton.addActionListener(
                e -> {
                    lifePanel.stopSimulation(startButton);
                    stopButton.setEnabled(false);
                });

        clearFieldButton.addActionListener(
                e -> {
                    synchronized (lifePanel.getLifeModel()) {
                        lifePanel.getLifeModel().clear();
                        lifePanel.repaint();
                    }
                });

        startButton.setSize(new Dimension(100, 100));
        clearFieldButton.setSize(new Dimension(100, 100));
        stopButton.setSize(new Dimension(200, 200));
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { // pass
        }

        SwingUtilities.invokeLater(() -> new LifeSimulation("Game of Life"));
    }
}
