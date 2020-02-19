package com.sergeiyarema;

public class Plant {
    private boolean state = false;

    public synchronized void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    @Override
    public synchronized String toString() {
        if (state) return "o";
        else return "x";
    }
}
