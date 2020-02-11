package com.sergeiyarema;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BarbershopQueue {
    private Queue<Custumer> queue = new LinkedList<>();
    private final Object bell = new Object();
    private Semaphore chair = new Semaphore(1);


    public void standInQueue(Custumer custumer) {
        queue.add(custumer);
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public void sitInChair(){
        try {
            chair.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void freeChair(){
        chair.release();
    }


}
