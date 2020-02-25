package main

import (
	"fmt"
	"math/rand"
	"time"
)

const SIZE = 500
const MAX = 100

var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

type sumArray struct {
	data       [10]int
	sum        int
}

func checkSum(arrays* [3]sumArray, diffs *[3][3]int) bool {
	for i := 0; i < 3; i++ {
		for j := 0; j < 3; j++ {
			(*diffs)[i][j] = (*arrays)[i].sum - (*arrays)[j].sum
		}
	}

	for i := 1; i < 3; i++ {
		if (*diffs)[0][i] != 0 {
			return true
		}
	}

	return false
}

func changeValue(array* [10]int, diffs* [3]int) {

	sum := 0

	for i:=0; i < 3; i++ {
		sum += (*diffs)[i]
	}

	index := random.Intn(10)

	if sum > 0 {
		(*array)[index] -= 1
	} else {
		(*array)[index] += 1
	}
}

func updater(array *sumArray, diffs* [3]int, done chan int) {

	changeValue(&array.data, diffs)

	(*array).sum = 0
	for i := 0; i < 3; i++ {
		(*array).sum += (*array).data[i]
	}

	done <- 1
}

func main() {
	var arrays [3]sumArray
	var diffs [3][3]int
	var done = make(chan int)

	for i := 0; i < len(arrays); i++ {
		sum := 0

		var resultArray [10]int

		for j := 0; j < 10; j++ {
			resultArray[j] = random.Intn(MAX)
			sum += resultArray[j]
		}

		arrays[i] = sumArray{
			data: resultArray,
			sum:  sum,
		}
	}

	fmt.Printf("%v\n\n", arrays)

	for checkSum(&arrays, &diffs) {

		fmt.Printf("%v\n\n", diffs)

		for i := 0; i < 3; i++ {
			go updater(&arrays[i], &diffs[i], done)
			fmt.Println(arrays[i].data)
		}

		<- done
	}

	fmt.Printf("%v\n\n", diffs)
}
