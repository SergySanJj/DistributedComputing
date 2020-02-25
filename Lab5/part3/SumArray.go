package main

import (
	"fmt"
	"math/rand"
	"time"
)

type SumArray struct {
	data [10]int
	sum  int
}

const upperLimit = 100

var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

func NewSumArray() *SumArray {
	var res = SumArray{}
	res.data = [10]int{}
	res.sum = 0
	return &res
}

func (sumArray *SumArray) Fill() {
	var sum = 0
	for i := range sumArray.data {
		sumArray.data[i] = random.Int() % upperLimit
		sum += sumArray.data[i]
	}
	sumArray.sum = sum
}

func (sumArray *SumArray) Replace() {
	var pos = random.Int() % len(sumArray.data)
	var change = random.Intn(2) == 0
	if change {
		if sumArray.data[pos] == upperLimit {
			return
		}
		sumArray.sum += 1
		sumArray.data[pos] += 1
	} else {
		if sumArray.data[pos] == 0 {
			return
		}
		sumArray.sum -= 1
		sumArray.data[pos] -= 1
	}
}

func (sumArray *SumArray) Print() {
	fmt.Println(sumArray.data, " ", sumArray.sum)
}
