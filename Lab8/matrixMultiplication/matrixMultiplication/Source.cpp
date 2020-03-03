#include <iostream>
#include <mpi.h>
#include "matrixFunctions.h"
#include "standatdMult.h"

using namespace std;

int main(int argc, char* argv[])
{
	int matrixN = 10;
	int testCount = 1000;
	StandardMult::test(testCount,matrixN);
	
}
