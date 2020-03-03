#pragma once
#include <chrono>
#include <iostream>


namespace CannonMult
{
	void cannonMatrixMultiplication(int n)
	{

	}

	void test(int testCount, int matrixN)
	{
		auto start = std::chrono::high_resolution_clock::now();
		for (int i = 0; i < testCount; i++)
		{
			cannonMatrixMultiplication(matrixN);
		}
		auto stop = std::chrono::high_resolution_clock::now();
		auto duration = std::chrono::duration_cast<std::chrono::microseconds>(stop - start);

		std::cout << duration.count();
	}
}
