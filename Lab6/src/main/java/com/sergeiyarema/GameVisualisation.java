package com.sergeiyarema;

import com.sergeiyarema.configs.Civilizations;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.*;

public class GameVisualisation extends JPanel {
    private int updateDelay = 100;
    private Thread[] civilizations;
    private GameModel life;
    private int cellSize = 1;
    private int cellGap = 1;

    public GameVisualisation() {
        setBackground(Color.WHITE);
        MouseAdapter ma =
                new MouseAdapter() {
                    private boolean pressedLeft = false;
                    private boolean pressedRight = false;
                    private boolean pressedWheel = false;

                    @Override
                    public void mouseDragged(MouseEvent e) {
                        setCell(e);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            pressedLeft = true;
                            pressedRight = false;
                            pressedWheel = false;
                            setCell(e);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            pressedLeft = false;
                            pressedRight = true;
                            pressedWheel = false;
                            setCell(e);
                        } else if (e.getButton() == MouseEvent.BUTTON2) {
                            pressedLeft = false;
                            pressedRight = false;
                            pressedWheel = true;
                            setCell(e);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            pressedLeft = false;
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            pressedRight = false;
                        } else if (e.getButton() == MouseEvent.BUTTON2) {
                            pressedWheel = false;
                        }
                    }

                    private void setCell(MouseEvent e) {
                        if (life != null) {

                            int row = e.getY() / (cellSize + cellGap);
                            int col = e.getX() / (cellSize + cellGap);
                            if (0 <= row && row < life.getHeight() && 0 <= col && col < life.getWidth()) {
                                if (pressedLeft) {
                                    life.setCell(row, col, 1);
                                    repaint();
                                }
                                if (pressedRight) {
                                    life.setCell(row, col, 2);
                                    repaint();
                                }
                                if (pressedWheel) {
                                    life.setCell(row, col, 0);
                                    repaint();
                                }
                            }
                        }
                    }
                };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    public GameModel getLifeModel() {
        return life;
    }

    @SuppressWarnings("SameParameterValue")
    public void initialize(int width, int height, int newCellSize) {
        cellSize = newCellSize;
        life = new GameModel(width, height);
    }

    public void startSimulation() {
        if (civilizations == null) {
            CyclicBarrier barrier = new CyclicBarrier(2, () -> {
                System.out.println("[s] paint");
                life.swapField();
                this.repaint();
                try {
                    Thread.sleep(updateDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("[e] paint");
            });

            civilizations = new CivilizationRunner[2];
            civilizations[0] = new CivilizationRunner(life, barrier, 1);
            civilizations[1] = new CivilizationRunner(life, barrier, 2);

            for (int i = 0; i < 2; i++) civilizations[i].start();
        }
    }

    public void stopSimulation() {
        if (civilizations != null)
            for (int i = 0; i < 2; i++) {
                civilizations[i].interrupt();
            }
        civilizations = null;
    }

    @Override
    public Dimension getPreferredSize() {
        if (life != null) {
            Insets b = getInsets();
            return new Dimension(
                    (cellSize + cellGap) * life.getWidth() + cellGap + b.left + b.right,
                    (cellSize + cellGap) * life.getHeight() + cellGap + b.top + b.bottom);
        } else return new Dimension(100, 100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (life != null) {
            super.paintComponent(g);
            Insets b = getInsets();
            for (int x = 0; x < life.getHeight(); x++) {
                for (int y = 0; y < life.getWidth(); y++) {
                    int c = life.getCell(x, y);
                    Color tmp = Civilizations.Colors.NEUTRAL;
                    if (c == 1) tmp = Civilizations.Colors.C1;
                    if (c == 2) tmp = Civilizations.Colors.C2;
                    g.setColor(tmp);

                    g.fillOval(b.left + cellGap + y * (cellSize + cellGap),
                            b.top + cellGap + x * (cellSize + cellGap),
                            cellSize,
                            cellSize);
                }
            }
        }
    }
}

