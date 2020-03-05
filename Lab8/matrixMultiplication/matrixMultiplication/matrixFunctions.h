#pragma once
#include <iostream>
#include <cstdlib>
#include <ctime>

double* generateMatrix(int n)
{
	double* matrix = new double[n*n];
	int i, j; 
	for (i = 0; i < n; i++)
	{
		for (j = 0; j < n; j++)
			matrix[i * n + j] = i;
	}
	return matrix;
}


double* createZeroMatrix(int n)
{
	double* matrix = new double[n * n];
	int i, j;
	for (i = 0; i < n; i++)
	{
		for (j = 0; j < n; j++)
			matrix[i * n + j] = 0;
	}
	return matrix;
}

void deleteMatrix(double* matrix)
{
	delete[] matrix;
}

void printMatrix(double* mat, int n)
{
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			std::cout << mat[i*n+j] << " ";
		}
		std::cout << std::endl;
	}
}