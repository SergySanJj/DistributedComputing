package com.sergeiyarema.matmul;

import java.util.Date;

public class DefaultMul implements IMatMul {

    @Override
    public double calculate(int matrixSize, String[] args) {
        double[] a = new double[matrixSize * matrixSize];
        double[] b = new double[matrixSize * matrixSize];
        double[] c = new double[matrixSize * matrixSize];

        Date startTime = new Date();
        matrixMultiplication(matrixSize, a, b, c);
        Date endTime = new Date();
        return (double) (endTime.getTime() - startTime.getTime());
    }

    private static void matrixMultiplication(int matrixDim, double[] a, double[] b, double[] c) {
        for (int i = 0; i < matrixDim; i++) {
            for (int j = 0; j < matrixDim; j++) {
                c[i * matrixDim + j] = 0;
                for (int k = 0; k < matrixDim; k++)
                    c[i * matrixDim + j] += a[i * matrixDim + k] * b[j * matrixDim + k];
            }
        }
    }
}
