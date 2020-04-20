package com.sergeiyarema.matmul;

import mpi.*;

public class Main {
    private static void printIfMain(String stringToPrint) {
        if (MPI.COMM_WORLD.Rank() == 0) {
            System.out.println(stringToPrint);
        }
    }

    private static void runAndPrint(String methodName, IMatMul controller, String[] args, int matrixDim) {
        System.out.println("\t" + methodName + ": " + controller.calculate(matrixDim, args));
        System.out.println();
    }

    public static void main(String[] args) throws MPIException {
        int[] dimensions = {100, 1000, 2500};
        for (int dim : dimensions) {
            printIfMain("Matrix size: " + dim);
            runAndPrint("Default", new DefaultMul(), args, dim);
            runAndPrint("Tape", new TapeMult(), args, dim);
            runAndPrint("Fox", new FoxMult(), args, dim);
            runAndPrint("Cannon", new CannonMult(), args, dim);
        }
    }
}