package main

import "fmt"

const SIZE = 500
const MAX = 100

func worker(pos int, g []SumArray, halt chan int, barrier *CyclicBarrier) {
	for {
		g[pos].Replace()

		barrier.Await(func() {
			if checkEquality(g) {
				halt <- 1
			}
		})

	}
}

func checkEquality(g []SumArray) bool {
	var firstVal = g[0].sum
	for i := 1; i < len(g); i++ {
		if firstVal != g[i].sum {
			return false
		}
	}
	return true
}

func Print(g []SumArray) {
	for _, e := range g {
		e.Print()
	}
	fmt.Println()
}

func main() {
	var g []SumArray
	var halt = make(chan int)
	var barrier = NewCyclicBarrier(1, func() {
		Print(g)
	})
	for i := 0; i < 3; i++ {
		var s = SumArray{}
		s.Fill()
		g = append(g, s)
	}

	for i := range g {
		go worker(i, g, halt, barrier)
	}

	<-halt

}
