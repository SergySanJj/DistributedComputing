package main

import (
	"math/rand"
	"time"
)

func getRandomMatrix(matrixDim int) [][]float64 {
	rand.Seed(time.Now().UnixNano())
	matrix := make([][]float64, matrixDim)
	for i := range matrix {
		matrix[i] = make([]float64, matrixDim)
	}
	for i := 0; i < matrixDim; i++ {
		for j := 0; j < matrixDim; j++ {
			matrix[i][j] = float64(rand.Intn(100))
		}
	}
	return matrix
}

func getEmptyMatrix(matrixDim int) [][]float64{
	res :=  make([][]float64, matrixDim)
	for i := range res {
		res[i] = make([]float64, matrixDim)
	}
	return res
}