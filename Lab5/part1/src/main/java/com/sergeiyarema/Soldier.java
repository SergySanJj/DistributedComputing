package com.sergeiyarema;

enum Positions {
    LEFT, MIDDLE, RIGHT
}

public class Soldier implements Runnable {
    private Positions position = Positions.MIDDLE;

    @Override
    public void run() {

    }

    public synchronized void turn() {
        if (position == Positions.LEFT)
            position = Positions.RIGHT;
        else position = Positions.LEFT;
    }

    public synchronized boolean checkCorrectPositioning(Soldier neighbour) {
        if (neighbour == null) return true;
        return position == neighbour.position;
    }
}
