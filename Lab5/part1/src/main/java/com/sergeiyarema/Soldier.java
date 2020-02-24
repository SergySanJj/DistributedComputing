package com.sergeiyarema;

import java.util.Random;

enum Positions {
    LEFT, MIDDLE, RIGHT
}

public class Soldier {
    private static Random random = new Random();
    private Positions position = Positions.MIDDLE;
    private boolean needToTurn = false;

    public synchronized void command() {
        if (random.nextBoolean()) {
            position = Positions.LEFT;
        } else position = Positions.RIGHT;
    }

    private synchronized void turn() {
        if (position == Positions.LEFT)
            position = Positions.RIGHT;
        else position = Positions.LEFT;
    }

    public synchronized boolean correctPositioning(Soldier neighbour) {
        if (neighbour == null) return true;
        if (position != neighbour.position) {
            needToTurn = true;
            return false;
        } else {
            needToTurn = false;
            return true;
        }
    }

    public void turnIfNeeded() {
        if (needToTurn) {
            turn();
        }
        needToTurn = false;
    }

    public synchronized int getStride() {
        if (position == Positions.LEFT)
            return -1;
        else return 1;
    }

    @Override
    public String toString() {
        switch (position) {
            case MIDDLE:
                return "^";
            case LEFT:
                return "<";
            case RIGHT:
                return ">";
        }
        return " ";
    }
}
