package com.sergeiyarema;

public class Marshal implements Runnable{
    private Soldier[] soldiers;

    public Marshal(Soldier[] soldiers) {
        this.soldiers = soldiers;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){

        }
    }
}
