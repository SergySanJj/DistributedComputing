package com.sergeiyarema;

import java.util.Random;

public class StringController implements Runnable {
    private static Random random = new Random();
    private int id;
    private String[] stringArray;
    private CyclicBarrier barrier;
    private CountController countController;

    public StringController(int id, String[] stringArray, CountController countController, CyclicBarrier barrier) {
        this.id = id;
        this.stringArray = stringArray;
        this.countController = countController;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !countController.isFinished()) {
            replace(random.nextInt(stringArray[id].length()));
            try {
                barrier.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    private void replace(int pos) {

        switch (stringArray[id].charAt(pos)) {
            case 'A':
                stringArray[id] = replace(stringArray[id], pos, 'C');
                countController.decCount(id);
                break;
            case 'C':
                stringArray[id] = replace(stringArray[id], pos, 'A');
                countController.incCount(id);
                break;
            case 'B':
                stringArray[id] = replace(stringArray[id], pos, 'D');
                countController.decCount(id);
                break;
            case 'D':
                stringArray[id] = replace(stringArray[id], pos, 'B');
                countController.incCount(id);
                break;
            default:
                break;
        }
    }

    private static String replace(String str, int pos, char to) {
        char[] chars = str.toCharArray();
        chars[pos] = to;
        return String.valueOf(chars);
    }
}
