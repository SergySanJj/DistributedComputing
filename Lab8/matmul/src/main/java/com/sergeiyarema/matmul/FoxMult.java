package com.sergeiyarema.matmul;

import mpi.MPI;
import mpi.MPIException;

import java.util.Date;

public class FoxMult implements IMatMul {
    private int master = 0;

    private double[] a;
    private double[] b;
    private double[] c;

    private int[] rows = new int[1];
    private int[] offset = new int[1];
    private int[] size = new int[1];

    @Override
    public double calculate(int matrixSize, String[] args) {
        MPI.Init(args);

        boolean[] option = new boolean[1];

        if (MPI.COMM_WORLD.Rank() == 0) {
            size[0] = matrixSize;
            if (args.length == 5)
                option[0] = true;
        }

        MPI.COMM_WORLD.Bcast(size, 0, 1, MPI.INT, master);
        MPI.COMM_WORLD.Bcast(option, 0, 1, MPI.BOOLEAN, master);

        Date startTime = new Date();

        initAndCompute(size[0]);

        Date endTime = new Date();

        MPI.Finalize();

        return (double)endTime.getTime() - startTime.getTime();
    }

    private void setup(int size) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                a[i * size + j] = i + j;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                b[i * size + j] = i - j;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                c[i * size + j] = 0;
    }

    private void compute(int size) {
        for (int k = 0; k < size; k++)
            for (int i = 0; i < rows[0]; i++)
                for (int j = 0; j < size; j++) {
                    c[i * size + k] += a[i * size + j] * b[j * size + k];
                }
    }

    private void initAndCompute(int size) throws MPIException {
        int myrank = MPI.COMM_WORLD.Rank();
        int nprocs = MPI.COMM_WORLD.Size();

        a = new double[size * size];
        b = new double[size * size];
        c = new double[size * size];

        int tagFromMaster = 1;
        if (myrank == 0) {
            setup(size);
            System.out.println("array a size: " + a.length);
            System.out.println("array b size: " + b.length);

            int averows = size / nprocs;
            int extra = size % nprocs;
            offset[0] = 0;

            for (int rank = 0; rank < nprocs; rank++) {
                rows[0] = (rank < extra) ? averows + 1 : averows;
                if (rank != 0) {
                    MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, rank, tagFromMaster);
                    MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, rank, tagFromMaster);
                    MPI.COMM_WORLD.Send(a, offset[0] * size, rows[0] * size, MPI.DOUBLE, rank, tagFromMaster);
                    MPI.COMM_WORLD.Send(b, 0, size * size, MPI.DOUBLE, rank, tagFromMaster);
                }
                offset[0] += rows[0];
            }

            compute(size);

            for (int source = 1; source < nprocs; source++) {
                MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, source, tagFromMaster);
                MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, source, tagFromMaster);
                MPI.COMM_WORLD.Recv(c, offset[0] * size, rows[0] * size, MPI.DOUBLE, source, tagFromMaster);
            }
        } else {
            MPI.COMM_WORLD.Recv(offset, 0, 1, MPI.INT, master, tagFromMaster);
            MPI.COMM_WORLD.Recv(rows, 0, 1, MPI.INT, master, tagFromMaster);
            MPI.COMM_WORLD.Recv(a, 0, rows[0] * size, MPI.DOUBLE, master, tagFromMaster);
            MPI.COMM_WORLD.Recv(b, 0, size * size, MPI.DOUBLE, master, tagFromMaster);

            compute(size);

            MPI.COMM_WORLD.Send(offset, 0, 1, MPI.INT, master, tagFromMaster);
            MPI.COMM_WORLD.Send(rows, 0, 1, MPI.INT, master, tagFromMaster);
            MPI.COMM_WORLD.Send(c, 0, rows[0] * size, MPI.DOUBLE, master, tagFromMaster);
        }
    }
}
