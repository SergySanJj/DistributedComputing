package main

import (
	"fmt"
	"time"
)

func tapeMul(A [][]float64, B [][]float64, C [][]float64, line int, forkJoin chan [][]float64) {
	if line > 0 {
		C = <-forkJoin
	}
	matrixDim := len(A)
	if line != matrixDim {
		res := make([]float64, matrixDim)
		var sum float64
		for j := 0; j < matrixDim; j++ {
			sum = 0
			for k := 0; k < len(A); k++ {
				sum += A[line][k] * B[k][j]
			}
			res[j] = sum
		}
		C[line] = res

		go tapeMul(A, B, C, line+1, forkJoin)

		forkJoin <- C
	}

	forkJoin <- C
}

func main() {
	matrixDim := 2500
	fmt.Println("Matrix dim:", matrixDim)

	A := getRandomMatrix(matrixDim)
	B := getRandomMatrix(matrixDim)
	C := getEmptyMatrix(matrixDim)

	forkJoin := make(chan [][]float64)

	start := time.Now()
	go tapeMul(A, B, C, 0, forkJoin)
	C = <-forkJoin
	elapsed := time.Since(start)

	fmt.Println("TapeMul:", elapsed.Milliseconds())
}
