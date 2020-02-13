package main

import (
	"fmt"
	"math/rand"
	"time"
)

const acquire = 1

var items = []string{"tobacco", "paper", "matches"}
var neededItem = 0

func controller(repeat int, tableSemaphore chan int, found <-chan int, halt chan<- int) {
	for i := 0; i < repeat; i++ {
		tableSemaphore <- acquire
		var item = rand.Intn(3)
		neededItem = item
		whatItemsArePut(item)
		<-tableSemaphore
		<-found
	}

	halt <- 0
}

func smoker(item int, tableSemaphore chan int, found chan<- int) {
	for true {
		tableSemaphore <- acquire

		if neededItem == item {
			fmt.Printf("Smoker %d smoking\n", item)
			time.Sleep(20 * time.Millisecond)
			found <- acquire
		}

		<-tableSemaphore
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
	var tableSemaphore = make(chan int, 1)
	var found = make(chan int, 1)
	var halt = make(chan int, 1)

	go controller(10, tableSemaphore, found, halt)

	for i := 0; i < 3; i++ {
		go smoker(i, tableSemaphore, found)
	}

	<-halt
}
