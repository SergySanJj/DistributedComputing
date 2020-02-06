package main

import (
	"fmt"
	"math/rand"
	"runtime"
	"time"
)

func main() {
	runtime.GOMAXPROCS(4)
	s1 := rand.NewSource(time.Now().UnixNano())
	r1 := rand.New(s1)

	const n = 16
	monks := make([]int, n)
	for i := range monks {
		monks[i] = r1.Intn(100)
	}
	fmt.Println(monks)

	res := make(chan int)
	go solve(0, n-1, monks, res)

	fmt.Println("Expected: ", maxOf(monks))
	fmt.Println("Result: ", <-res)
	fmt.Println(monks)
}

func solve(start int, end int, monks []int, ch chan int) {
	var len = end - start
	if len == 0 {
		ch <- end
	} else if len == 1 {
		fmt.Println("Fight ", monks[start], monks[end])
		ch <- max(monks[start], monks[end])
	} else {
		left := make(chan int)
		right := make(chan int)
		go solve(start, (end+start)/2, monks, left)
		go solve((end+start)/2+1, end, monks, right)
		var lres = <-left
		var rres = <-right
		fmt.Println("Fight ", lres,rres)
		ch <- max(lres, rres)
	}
}

func max(x, y int) int {
	if x < y {
		return y
	}
	return x
}

func maxOf(arr []int) int {
	var currMax = -1
	for i := range arr {
		if currMax < arr[i] {
			currMax = arr[i]
		}
	}
	return currMax
}
