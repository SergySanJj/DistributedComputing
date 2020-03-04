#include <iostream>
#include <mpi.h>
#include "matrixFunctions.h"
#include "standatdMult.h"
#include "tapeMult.h"

using namespace std;

int main(int argc, char* argv[])
{
	int matrixN = 500;

	MPI_Init(&argc, &argv);

	StandardMult::standardMatrixMultiplication(matrixN);
	TapeMult::tapeMatrixMultiplication(matrixN, argc, argv);
	MPI_Finalize();
}
