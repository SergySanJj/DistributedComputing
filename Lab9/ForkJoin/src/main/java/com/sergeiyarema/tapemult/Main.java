package com.sergeiyarema.tapemult;

import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] argv) {
        int matrixDim = 100;
        System.out.println("Matrix dim: " + matrixDim);
        Double[][] A = MatrixMisc.getRandomMatrix(matrixDim);
        Double[][] B = MatrixMisc.getRandomMatrix(matrixDim);
        Double[][] C = MatrixMisc.getEmptyMatrix(matrixDim);

        long start = System.currentTimeMillis();
        new ForkJoinPool().invoke(new TapeMult(A, B, C, 0));
        long stop = System.currentTimeMillis();

        System.out.println("TapeMult: " + (stop - start));
    }
}
