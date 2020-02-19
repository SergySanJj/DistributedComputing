package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func priceUpdater(graph Graph, readWriteLock sync.RWMutex) {
	for {
		readWriteLock.RLock()
		N := len(graph)
		if N != 0 {
			i := rand.Intn(N)
			if len(graph[i]) != 0 {
				j := rand.Intn(len(graph[i]))

				graph[i][j].price = generatePrice() + 1
				fmt.Println("Update ", i, " -> ", graph[i][j].to, "\t\tprice\t", graph[i][j].price)
			}
		}
		readWriteLock.RUnlock()
		randomWait()
	}
}

func edgeUpdater(graph Graph, readWriteLock sync.RWMutex) {
	for {
		readWriteLock.Lock()
		N := len(graph)
		if rand.Intn(2) == 0 {
			graph = generateNewEdge(graph, N)
		} else {
			graph = removeEdge(graph, N)
		}
		readWriteLock.Unlock()
		randomWait()
	}
}

func townUpdater(graph Graph, mutex sync.RWMutex) {
	for {
		mutex.Lock()
		if rand.Intn(2) == 0 {
			graph = addTown(graph)
		} else {
			graph = removeTown(graph)
		}
		mutex.Unlock()
		randomWait()
	}
}

func priceFinder(graph Graph, mutex sync.RWMutex) {
	for {
		mutex.RLock()
		N := numberOfTowns(len(graph))
		i := rand.Intn(N)
		j := rand.Intn(N)
		if i != j {
			price := findPath(graph, i, j)
			if price == 0 {
				fmt.Println("No path ", i, " -> ", j)
			} else {
				fmt.Println(i, " -> ", j, "\t\tprice\t", price)
			}
		}
		mutex.RUnlock()
		randomWait()
	}
}

func randomWait() {
	time.Sleep(time.Millisecond*100)
}

func main() {
	N := rand.Intn(8) + 5
	graph := generateGraph(N)
	printGraph(graph)
	readWriteLock := sync.RWMutex{}
	quit := make(chan bool)

	go priceUpdater(graph, readWriteLock)
	go edgeUpdater(graph, readWriteLock)
	go townUpdater(graph, readWriteLock)
	go priceFinder(graph, readWriteLock)

	<-quit
}
