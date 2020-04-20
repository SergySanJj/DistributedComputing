#include <iostream>
#include <tbb/parallel_for.h>
#include <tbb/blocked_range.h>
#include <ctime>
#include <chrono>

using namespace tbb;
using namespace std;
using namespace chrono;

const int matrixDim = 100;

double A[matrixDim][matrixDim];
double B[matrixDim][matrixDim];
double C[matrixDim][matrixDim];

class MatrixMultiplication {
public:
    void operator()(blocked_range<int> range) const {
        for (int i = range.begin(); i != range.end(); ++i) {
            for (int j = 0; j < matrixDim; ++j) {
                for (int k = 0; k < matrixDim; ++k) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }
};


int main() {
    std::srand(unsigned(time(nullptr)));

    cout << "Matrix dim: " << matrixDim;

    for (int i = 0; i < matrixDim; i++) {
        for (int j = 0; j < matrixDim; j++) {
            A[i][j] = 1;
            B[i][j] = rand() + i * j;
            C[i][j] = 0;
        }
    }

    auto start = high_resolution_clock::now();
    parallel_for(blocked_range<int>(0, matrixDim), MatrixMultiplication());
    auto stop = high_resolution_clock::now();

    cout << "TapeMul: " << (double) duration_cast<milliseconds>(stop - start).count();

    return 0;
}