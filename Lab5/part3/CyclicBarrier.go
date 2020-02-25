package main

import "sync"

type CyclicBarrier struct {
	action     func()
	generation int
	count      int
	parties    int
	trip       *sync.Cond
}

func (b *CyclicBarrier) nextGeneration() {
	// signal completion of last generation
	b.trip.Broadcast()
	b.count = b.parties
	// set up next generation
	b.generation++
}

func (b *CyclicBarrier) Await() {
	b.trip.L.Lock()
	defer b.trip.L.Unlock()

	generation := b.generation

	b.count--
	index := b.count
	//println(index)

	if index == 0 {
		b.nextGeneration()
	} else {
		for generation == b.generation {
			//wait for current generation complete
			b.trip.Wait()
		}
		b.action()
	}
}

func NewCyclicBarrier(num int, action func()) *CyclicBarrier {
	b := CyclicBarrier{}
	b.action = action
	b.count = num
	b.parties = num
	b.trip = sync.NewCond(&sync.Mutex{})

	return &b
}
