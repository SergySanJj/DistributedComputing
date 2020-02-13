package main

import (
	"fmt"
	"math/rand"
	"time"
)

const acquire = 1

var items = []string{"tobacco", "paper", "matches"}
var neededItem = 0

func controller(repeat int, table chan int, smoke <-chan int, halt chan<- int) {
	for i := 0; i < repeat; i++ {
		table <- acquire
		var item = rand.Intn(3)
		neededItem = item
		whatItemsArePut(item)
		<-table
		<-smoke
	}

	halt <- 0
}

func smoker(item int, table chan int, smoke chan<- int) {
	for true {
		table <- acquire

		if neededItem == item {
			fmt.Printf("Smoker %d smoking\n", item)
			time.Sleep(20 * time.Millisecond)
			smoke <- acquire
		}

		<-table
	}
}

func whatItemsArePut(item int) {
	for i := 0; i < 3; i++ {
		if item != i {
			fmt.Println("Put " + items[i])
		}
	}
}

func main() {
	var table = make(chan int, 1)
	var smoke = make(chan int, 1)
	var halt = make(chan int, 1)

	go controller(10, table, smoke, halt)

	for i := 0; i < 3; i++ {
		go smoker(i, table, smoke)
	}

	<-halt
}
