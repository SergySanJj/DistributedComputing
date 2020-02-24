package com.sergeiyarema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CountController {
    private AtomicInteger[] countSum;

    private volatile boolean isFinished = false;

    public CountController(String[] strings) {
        this.countSum = new AtomicInteger[strings.length];

        for (int i = 0; i < strings.length; i++) {
            this.countSum[i] = new AtomicInteger(0);
            for (char c : strings[i].toCharArray()) {
                if (c == 'A' || c == 'B')
                    incCount(i);
            }
        }
    }

    public void incCount(int id) {
        synchronized (countSum[id]) {
            this.countSum[id].incrementAndGet();
        }
    }

    public void decCount(int id) {
        synchronized (countSum[id]) {
            this.countSum[id].decrementAndGet();
        }
    }

    public synchronized void updateEquality() {
        Map<Integer, Integer> mapper = new HashMap<>();
        for (AtomicInteger i : countSum) {
            if (mapper.get(i.get()) == null) {
                mapper.put(i.get(), 1);
            } else {
                int val = mapper.get(i.get()) + 1;
                mapper.put(i.get(), val);
            }
        }
        isFinished = false;
        for (Map.Entry<Integer, Integer> entry : mapper.entrySet()) {
            if (entry.getValue() >= 3) {
                isFinished = true;
                return;
            }
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public String toString() {
        return Arrays.toString(countSum);
    }
}
