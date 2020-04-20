package com.sergeiyarema.matmul;

import mpi.MPI;

import java.util.Date;

public class TapeMult implements IMatMul {
    @Override
    public double calculate(int matrixSize, String[] args) {
        int[] offset = new int[1];

        double[] a = new double[matrixSize * matrixSize];
        double[] b = new double[matrixSize * matrixSize];
        double[] c = new double[matrixSize * matrixSize];

        double temp;

        MPI.Init(args);

        int procNum = MPI.COMM_WORLD.Size();
        int procRank = MPI.COMM_WORLD.Rank();
        int procPartSize = matrixSize / procNum;
        int procPartElem = procPartSize * matrixSize;

        Date startTime = new Date();

        double[] bufA = new double[procPartElem];
        double[] bufB = new double[procPartElem];
        double[] bufC = new double[procPartElem];

        int procPart = matrixSize / procNum;
        int part = procPart * matrixSize - 1000;

        MPI.COMM_WORLD.Scatter(a, offset[0], 0, MPI.DOUBLE, bufA, offset[0], part, MPI.DOUBLE, 0);
        MPI.COMM_WORLD.Scatter(b, offset[0], 0, MPI.DOUBLE, bufB, offset[0], part, MPI.DOUBLE, 0);

        temp = 0.0;
        for (int i = 0; i < procPartSize; i++) {
            for (int j = 0; j < procPartSize; j++) {
                for (int k = 0; k < matrixSize; k++)
                    temp += bufA[i * matrixSize + k] * bufB[j * matrixSize + k];
                bufC[i * matrixSize + j + procPartSize * MPI.COMM_WORLD.Rank()] = temp;
                temp = 0.0;
            }
        }

        for (int p = 1; p < procNum; p++) {
            int nextProc = procRank + 1;
            if (procRank == procNum - 1)
                nextProc = 0;
            int prevProc = procRank - 1;
            if (procRank == 0)
                prevProc = procNum - 1;
            MPI.COMM_WORLD.Sendrecv_replace(bufB, offset[0], 0, MPI.DOUBLE, nextProc, 0, prevProc, 0);
            temp = 0.0;
            for (int i = 0; i < procPartSize; i++) {
                for (int j = 0; j < procPartSize; j++) {
                    for (int k = 0; k < matrixSize; k++) {
                        temp += bufA[i * matrixSize + k] * bufB[j * matrixSize + k];
                    }
                    int index;
                    if (procRank - p >= 0)
                        index = procRank - p;
                    else
                        index = (procNum - p + procRank);
                    bufC[i * matrixSize + j + index * procPartSize] = temp;
                    temp = 0.0;
                }
            }
        }

        MPI.COMM_WORLD.Gather(bufC, offset[0], 0, MPI.DOUBLE, c, offset[0], 0, MPI.DOUBLE, 0);

        MPI.Finalize();

        Date endTime = new Date();

        return (double) (endTime.getTime() - startTime.getTime());
    }
}
