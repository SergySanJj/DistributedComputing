#include <iostream>
#include <mpi.h>
#include "matrixFunctions.h"
#include "standatdMult.h"
#include "tapeMult.h"

using namespace std;

int main(int argc, char* argv[])
{
	int matrixN = 10;
	int testCount = 1;
	// StandardMult::test(testCount,matrixN);
	TapeMult::test(testCount, matrixN, argc, argv);
}
