package com.sergeiyarema;

import java.util.LinkedList;
import java.util.Queue;

public class BarbershopQueue {
    private Queue<Custumer> queue = new LinkedList<>();
    private final Object bell = new Object();

    public void standInQueue(Custumer custumer) {
        queue.add(custumer);
    }


}
