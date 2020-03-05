#include <iostream>
#include <mpi.h>
#include "matrixFunctions.h"
#include "standatdMult.h"
#include "tapeMult.h"

using namespace std;


int main(int argc, char* argv[])
{
	int matrixN = 4;
	int node = 0;

	MPI_Init(&argc, &argv);

	MPI_Comm_rank(MPI_COMM_WORLD, &node);
	if (node == 0) {
		StandardMult::standardMatrixMultiplication(matrixN);
	}

	TapeMult::tapeMatrixMultiplication(matrixN);
	MPI_Finalize();
}
