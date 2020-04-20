package com.sergeiyarema.tapemult;

import java.util.concurrent.*;

public class TapeMult extends RecursiveTask<Double[][]> {
    private Double[][] A;
    private Double[][] B;
    private Double[][] C;
    private int currentPos;

    public TapeMult(Double[][] A, Double[][] B, Double[][] C, int currentPos) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.currentPos = currentPos;
    }

    @Override
    protected Double[][] compute() {
        if (currentPos != A.length) {
            Double[] res = new Double[A.length];
            double sum;
            for (int j = 0; j < A.length; j++) {
                sum = 0;
                for (int k = 0; k < A.length; k++) {
                    sum += A[currentPos][k] * B[k][j];
                }
                res[j] = sum;
            }
            C[currentPos] = res;

            TapeMult mult = new TapeMult(A, B, C, currentPos + 1);
            mult.fork();

            C = mult.join();
        }

        return C;
    }
}