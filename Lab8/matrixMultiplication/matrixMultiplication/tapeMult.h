#pragma once
#include <chrono>
#include <iostream>
#include <mpi.h>

namespace TapeMult
{

	int** mult(int ** matA, int ** matB, int n)
	{
		// MPI_Init(&argc, &argv); // TODO: connect mpi calls
		int node = 0;
		MPI_Comm_rank(MPI_COMM_WORLD, &node);

		if (node == 0)
		{
			std::cout << "control node\n";
		}

		std::cout << "Hi Nikita from " << node << std::endl;
		MPI_Finalize();
	}

	void tapeMatrixMultiplication(int n)
	{
		
	}

	void test(int testCount, int matrixN)
	{
		auto start = std::chrono::high_resolution_clock::now();
		for (int i = 0; i < testCount; i++)
		{
			tapeMatrixMultiplication(matrixN);
		}
		auto stop = std::chrono::high_resolution_clock::now();
		auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);

		std::cout << duration.count();
	}
}
