package com.sergeiyarema.matmul;

import mpi.Cartcomm;
import mpi.MPI;

import java.util.Date;

public class CannonMult implements  IMatMul {
    @Override
    public double calculate(int matrixSize, String[] args) {
        MPI.Init(args);
        MPI.COMM_WORLD.Barrier();

        int p = MPI.COMM_WORLD.Size();
        int p_sqrt = (int) Math.sqrt(p);

        Date startTime = new Date( );

        int[] dim = new int[2];
        dim[0] = dim[1] = p_sqrt;
        boolean[] period = new boolean[2];
        period[0] = period[1] = true;
        Cartcomm cart = MPI.COMM_WORLD.Create_cart(dim, period, false);
        MPI.COMM_WORLD.Rank();

        double[] a = new double[matrixSize * matrixSize];
        double[] b = new double[matrixSize * matrixSize];

        int sourse;
        int dest;

        int[] coords = new int[2];
        dest = cart.Shift(coords[0], 1).rank_dest;
        sourse = cart.Shift(coords[0], 1).rank_source;
        int[] offset = new int[1];
        MPI.COMM_WORLD.Sendrecv_replace(a, offset[0], matrixSize, MPI.DOUBLE, dest, 0, sourse, 0);

        dest = cart.Shift(coords[1], 1).rank_dest;
        sourse = cart.Shift(coords[1], 1).rank_source;
        MPI.COMM_WORLD.Sendrecv_replace(b, offset[0], matrixSize, MPI.DOUBLE, dest, 0, sourse, 0);

        for (int i = 0; i < p_sqrt; i++) {
            dest = cart.Shift(1, 1).rank_dest;
            sourse = cart.Shift(1, 1).rank_source;
            MPI.COMM_WORLD.Sendrecv_replace(a, offset[0], matrixSize, MPI.DOUBLE, dest, 0, sourse, 0);

            dest = cart.Shift(0, 1).rank_dest;
            sourse = cart.Shift(0, 1).rank_source;
            MPI.COMM_WORLD.Sendrecv_replace(b, offset[0], matrixSize, MPI.DOUBLE, dest, 0, sourse, 0);
        }

        MPI.Finalize();

        Date endTime = new Date( );

        return (double)endTime.getTime() - startTime.getTime();
    }
}
