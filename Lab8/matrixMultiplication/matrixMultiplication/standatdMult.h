#pragma once
#include <chrono>
#include <algorithm> 
#include <iostream>

#include "matrixFunctions.h"

namespace StandardMult
{
	double** matmul(double** matA, double** matB, int n)
	{
		double** res = createZeroMatrix(n);

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < n; k++)
					res[i][j] += matA[i][k] * matB[k][j];

		return res;
	}

	void standardMatrixMultiplication(int n)
	{
		MPI_Barrier(MPI_COMM_WORLD);

		int ProcRank = 0;
		MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);


		if (ProcRank == 0)
		{
			double** matrixA = generateMatrix(n);
			double** matrixB = generateMatrix(n);

			double start = MPI_Wtime();

			double** mult = matmul(matrixA, matrixB, n);

			double finish = MPI_Wtime();
			double duration = finish - start;

			printf("[Standard] Time of execution = %f\n", duration);

			deleteMatrix(matrixA, n);
			deleteMatrix(matrixB, n);
			deleteMatrix(mult, n);
		}

		MPI_Barrier(MPI_COMM_WORLD);
	}
}
