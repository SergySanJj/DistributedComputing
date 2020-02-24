package com.sergeiyarema;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int arraySize = 100;
        int threadCount = 2;
        int chunkSize = arraySize / threadCount;

        Soldier[] soldiers = new Soldier[arraySize];
        initSoldiers(soldiers);

        List<Captain> captains = new ArrayList<>();
        CyclicBarrier barrier = new CyclicBarrier(
                arraySize / chunkSize,
                () -> syncCaptains(captains, soldiers)
        );

        assignMarshals(arraySize, chunkSize, soldiers, captains, barrier);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (Captain captain : captains) {
            executorService.execute(captain);
        }
        executorService.shutdown();
    }

    private static void assignMarshals(int arraySize, int chunkSize,
                                       Soldier[] soldiers,
                                       List<Captain> captains,
                                       CyclicBarrier barrier) {
        int unassignedSoldiers = arraySize;
        while (unassignedSoldiers > 0) {
            int start = arraySize - unassignedSoldiers;
            int end = start + chunkSize - 1;
            captains.add(new Captain(soldiers, start, end, barrier));
            unassignedSoldiers -= chunkSize;
        }
    }

    private static void initSoldiers(Soldier[] soldiers) {
        for (int i = 0; i < soldiers.length; i++) {
            soldiers[i] = new Soldier();
        }
    }

    private static void syncCaptains(List<Captain> captains, Soldier[] soldiers) {
        boolean aligned = true;
        for (Captain captain : captains) {
            if (captain.getNeedToBeTurned() != 0) {
                aligned = false;
                break;
            }
        }
        Captain.setAllAligned(aligned);
        printSoldiers(soldiers);
    }

    private static void printSoldiers(Soldier[] soldiers) {
        for (Soldier soldier : soldiers) {
            System.out.print(soldier);
        }
        System.out.println();
    }

}
