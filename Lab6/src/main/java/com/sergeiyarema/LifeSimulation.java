package com.sergeiyarema;

import com.sergeiyarema.configs.Visualisation;

import javax.swing.*;
import java.awt.*;

public class LifeSimulation extends JFrame {
    private GameVisualisation gameVisualisation;
    private JButton startButton;
    private JButton stopButton;
    private JButton clearFieldButton;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { // pass
        }

        SwingUtilities.invokeLater(() -> new LifeSimulation("Game of life"));
    }

    private LifeSimulation(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        gameVisualisation = new GameVisualisation();
        gameVisualisation.initialize(
                Visualisation.cellsHorizontal,
                Visualisation.cellsVertical,
                Visualisation.cellSize
        );
        add(gameVisualisation);

        configureToolbar();
        pack();
        setVisible(true);
    }

    private void configureToolbar() {
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
                    gameVisualisation.startSimulation();
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                });

        stopButton.addActionListener(
                e -> {
                    gameVisualisation.stopSimulation();
                    stopButton.setEnabled(false);
                    startButton.setEnabled(true);
                });

        clearFieldButton.addActionListener(
                e -> {
                    synchronized (gameVisualisation.getLifeModel()) {
                        gameVisualisation.getLifeModel().clear();
                        gameVisualisation.repaint();
                    }
                });

        startButton.setSize(new Dimension(100, 100));
        clearFieldButton.setSize(new Dimension(100, 100));
        stopButton.setSize(new Dimension(200, 200));
    }


}
