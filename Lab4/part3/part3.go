package main

import (
	"fmt"
	"math"
	"math/rand"
	"sync"
	"time"
)

type Edge struct {
	to    int
	price int
}

func generateGraph(N int) [][]Edge {
	graph := make([][]Edge, N)
	visited := make([][]bool, N)
	for i := 0; i < N; i++ {
		graph[i] = make([]Edge, 0)
		visited[i] = make([]bool, N)
		for j := 0; j < N; j++ {
			visited[i][j] = false
			if i == j {
				visited[i][j] = true
			}
		}
	}
	for i := 0; i < N; i++ {
		for j := 0; j < N; j++ {
			price := ridePrice()
			if (rand.Intn(2) == 0) && (price != 0) && !visited[i][j] {
				graph[i] = append(graph[i], Edge{j, price})
				graph[j] = append(graph[j], Edge{i, price})
				visited[i][j] = true
				visited[j][i] = true
			}
		}
	}
	printGraph(graph)
	return graph
}

func ridePrice() int {
	return rand.Intn(10)
}

func getNumberOfTowns(graphLength int) int {
	return int(math.Sqrt(float64(1+8*graphLength))-1)/2 + 1
}

func changePrice(graph [][]Edge, readWriteLock sync.RWMutex) {
	for {
		readWriteLock.RLock()
		N := len(graph)
		if N != 0 {
			i := rand.Intn(N)
			if len(graph[i]) != 0 {
				j := rand.Intn(len(graph[i]))

				graph[i][j].price = ridePrice() + 1
				fmt.Println("Update ", i, " -> ", graph[i][j].to, "     price ", graph[i][j].price)
			}
		}
		readWriteLock.RUnlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10)+1))
	}
}

func changeRides(graph [][]Edge, readWriteLock sync.RWMutex) {
	for {
		readWriteLock.Lock()
		N := len(graph)
		if rand.Intn(2) == 0 {
			graph = generateNewPath(graph, N)
		} else {
			graph = removeRide(graph, N)
		}
		readWriteLock.Unlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10)+1))
	}
}

func generateNewPath(graph [][]Edge, N int) [][]Edge {
	if N == 0 {
		return graph
	}
	visited := make([]bool, N)
	for i := 0; i < N; i++ {
		visited[i] = false
	}
	i := rand.Intn(N)
	for j := 0; j < len(graph[i]); j++ {
		if !visited[graph[i][j].to] {
			visited[graph[i][j].to] = true
		}
	}
	for j := 0; j < N; j++ {
		if !visited[j] {
			price := ridePrice() + 1
			graph[i] = append(graph[i], Edge{j, price})
			fmt.Println("New path ", i, " -> ", j, "      price ", price)
			break
		}
	}

	return graph
}

func removeRide(graph [][]Edge, N int) [][]Edge {
	if N != 0 {
		i := rand.Intn(N)
		if len(graph[i]) != 0 {
			j := rand.Intn(len(graph[i]))
			fmt.Println("Remove path ", i, " -> ", graph[i][j].to)
			graph[i] = append(graph[i][:j], graph[i][j+1:]...)
		}
	}
	return graph
}

func changeTowns(graph [][]Edge, mutex sync.RWMutex) {
	for {
		mutex.Lock()
		if rand.Intn(2) == 0 {
			graph = addTown(graph)
		} else {
			graph = removeTown(graph)
		}
		mutex.Unlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10)+1))
	}
}

func addTown(graph [][]Edge) [][]Edge {
	newTown := make([]Edge, 0)
	graph = append(graph, newTown)
	fmt.Println("New town ", len(graph)-1)
	return graph
}

func removeTown(graph [][]Edge) [][]Edge {
	N := len(graph)
	town := rand.Intn(N)
	graph = append(graph[:town], graph[town+1:]...)
	for i := 0; i < N-1; i++ {
		for j := 0; j < len(graph[i]); j++ {
			if graph[i][j].to == town {
				graph[i] = append(graph[i][:j], graph[i][j+1:]...)
			} else if graph[i][j].to > town {
				graph[i][j].to--
			}
		}
	}
	fmt.Println("Remove town ", town)
	fmt.Println(graph)
	return graph
}

func pathPrice(graph [][]Edge, mutex sync.RWMutex) {
	for {
		mutex.RLock()
		N := getNumberOfTowns(len(graph))
		i := rand.Intn(N)
		j := rand.Intn(N)
		if i != j {
			price := findPath(graph, i, j)
			if price == 0 {
				fmt.Println("No path ", i, " -> ", j)
			} else {
				fmt.Println(i, " -> ", j, " price: ", price)
			}
		}
		mutex.RUnlock()
		time.Sleep(time.Second * time.Duration(rand.Intn(10)+1))
	}
}

func findPath(graph [][]Edge, from int, to int) int {
	INF := math.MaxInt64
	N := len(graph)
	visited := make([]bool, N)
	prices := make([]int, N)
	for i := 0; i < N; i++ {
		visited[i] = false
		prices[i] = INF
	}

	prices[from] = 0

	for i := 0; i < N; i++ {
		v := -1
		for j := 0; j < N; j++ {
			if !visited[j] && (v == -1 || prices[j] < prices[v]) {
				v = j
			}
		}
		if prices[v] == INF {
			break
		}
		visited[v] = true
		for j := 0; j < len(graph[i]); j++ {
			to := graph[i][j].to
			price := graph[i][j].price
			if prices[v]+price < prices[to] {
				prices[to] = prices[v] + price
			}
		}
	}
	if prices[to] == INF {
		return 0
	}
	return prices[to]
}

func printGraph(graph [][]Edge) {
	for i := 0; i < len(graph); i++ {
		fmt.Println(i, ": ", graph[i])
	}
}

func main() {
	N := rand.Intn(8) + 5
	graph := generateGraph(N)
	readWriteLock := sync.RWMutex{}
	quit := make(chan bool)

	go changePrice(graph, readWriteLock)
	go changeRides(graph, readWriteLock)
	go changeTowns(graph, readWriteLock)
	go pathPrice(graph, readWriteLock)
	<-quit
}
