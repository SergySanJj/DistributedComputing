package com.sergeiyarema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int stringsLen = 20;
        int stringCount = 5;
        Random random = new Random();

        String[] stringArray = new String[stringCount];
        for (int i = 0; i < stringCount; i++) {
            char[] chars = new char[stringsLen];
            for (int j = 0; j < stringsLen; j++) {
                chars[j] = (char) (random.nextInt(4) + 'A');
            }
            stringArray[i] = String.valueOf(chars);
        }
        CountController countController = new CountController(stringArray);
        List<StringController> stringControllers = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(stringCount);

        CyclicBarrier barrier = new CyclicBarrier(stringCount, () -> {
            countController.updateEquality();
            System.out.println(Arrays.toString(stringArray) + " " + countController);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        for (int i = 0; i < stringCount; i++) {
            stringControllers.add(new StringController(i, stringArray, countController, barrier));
        }
        for (StringController stringController : stringControllers)
            executorService.execute(stringController);
        executorService.shutdown();
    }
}
