#pragma once
#include <chrono>
#include <algorithm> 
#include <iostream>

#include "matrixFunctions.h"

namespace StandardMult
{
	void matmul(double* matA, double* matB, int n, double* res)
	{
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				for (int k = 0; k < n; k++)
					res[i*n+j] += matA[i*n+k] * matB[k*n+j];
	}

	void standardMatrixMultiplication(int n)
	{
		MPI_Barrier(MPI_COMM_WORLD);

		int ProcRank = 0;
		MPI_Comm_rank(MPI_COMM_WORLD, &ProcRank);


		if (ProcRank == 0)
		{
			double* matrixA = generateMatrix(n);
			double* matrixB = generateMatrix(n);
			double* res = createZeroMatrix(n);

			double start = MPI_Wtime();

			matmul(matrixA, matrixB, n, res);

			double finish = MPI_Wtime();
			double duration = finish - start;

			printf("[Standard] Time of execution = %f\n", duration);
			printMatrix(res, n);

			deleteMatrix(matrixA);
			deleteMatrix(matrixB);
			deleteMatrix(res);
		}

		MPI_Barrier(MPI_COMM_WORLD);
	}
}
