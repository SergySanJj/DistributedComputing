package com.sergeiyarema.tapemult;

import java.util.Random;

public class MatrixMisc {
    private static Random random = new Random();

    public static Double[][] getRandomMatrix(int matrixDim) {
        Double[][] matrix = new Double[matrixDim][matrixDim];
        for (int i = 0; i < matrixDim; i++) {
            for (int j = 0; j < matrixDim; j++) {
                matrix[i][j] = (double) random.nextInt(100);
            }
        }
        return matrix;
    }
    public static Double[][] getEmptyMatrix(int matrixDim){
        return new Double[matrixDim][matrixDim];
    }

    private MatrixMisc(){}
}
