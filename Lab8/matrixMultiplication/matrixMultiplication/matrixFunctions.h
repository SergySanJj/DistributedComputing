#pragma once
#include <iostream>
#include <cstdlib>
#include <ctime>

double** generateMatrix(int n)
{
	std::srand(unsigned(std::time(0)));
	double** matrix = new double* [n];
	for (int i = 0; i < n; i++)
	{
		matrix[i] = new double[n];
		for (int j = 0; j < n; j++)
		{
			matrix[i][j] = std::rand()%10;
		}
	}
	return matrix;
}

double** createZeroMatrix(int n)
{
	double** matrix = new double* [n];
	for (int i = 0; i < n; i++)
	{
		matrix[i] = new double[n];
		for (int j = 0; j < n; j++)
		{
			matrix[i][j] = 0;
		}
	}
	return matrix;
}

void deleteMatrix(double** matrix, int n)
{
	for (int i=0;i<n;i++)
	{
		delete[] matrix[i];
	}
	delete[] matrix;
}

void printMatrix(double** mat, int n)
{
	for (int i = 0; i < n; i++)
	{
		for (int j = 0; j < n; j++)
		{
			std::cout << mat[i][j] << " ";
		}
		std::cout << std::endl;
	}
}