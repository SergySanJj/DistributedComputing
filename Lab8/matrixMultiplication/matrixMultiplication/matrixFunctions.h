#pragma once
#include <iostream>
#include <cstdlib>
#include <ctime>

int** generateMatrix(int n)
{
	std::srand(unsigned(std::time(0)));
	int** matrix = new int* [n];
	for (int i = 0; i < n; i++)
	{
		matrix[i] = new int[n];
		for (int j = 0; j < n; j++)
		{
			matrix[i][j] = std::rand()%10;
		}
	}
	return matrix;
}

int** createZeroMatrix(int n)
{
	int** matrix = new int* [n];
	for (int i = 0; i < n; i++)
	{
		matrix[i] = new int[n];
		for (int j = 0; j < n; j++)
		{
			matrix[i][j] = 0;
		}
	}
	return matrix;
}

void deleteMatrix(int** matrix, int n)
{
	for (int i=0;i<n;i++)
	{
		delete[] matrix[i];
	}
	delete[] matrix;
}

void printMatrix(int** mat, int n)
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