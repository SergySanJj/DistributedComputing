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
		double** matrixA = generateMatrix(n);
		double** matrixB = generateMatrix(n);


		double** mult = matmul(matrixA, matrixB, n);
		// printMatrix(mult, n);

		deleteMatrix(matrixA, n);
		deleteMatrix(matrixB, n);
		deleteMatrix(mult, n);
	}

	void test(int testCount, int matrixN)
	{
		auto start = std::chrono::high_resolution_clock::now();
		for (int i=0;i<testCount;i++)
		{
			standardMatrixMultiplication(matrixN);
		}
		auto stop = std::chrono::high_resolution_clock::now();
		auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);

		std::cout << duration.count();
	}
}
