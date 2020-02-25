package main

import "sync"

type CyclicBarrier struct {
	generation int
	count      int
	parties    int
	condition  *sync.Cond
	f          func()
}

func (b *CyclicBarrier) nextGeneration() {
	b.condition.Broadcast()
	b.count = b.parties
	b.generation++
}

func (b *CyclicBarrier) Await(action func()) {
	b.condition.L.Lock()

	defer b.condition.L.Unlock()
	defer b.f()
	defer action()

	generation := b.generation

	b.count--
	index := b.count

	if index == 0 {
		b.nextGeneration()
	} else {
		for generation == b.generation {
			b.condition.Wait()
		}
	}
}

func NewCyclicBarrier(num int, f func()) *CyclicBarrier {
	b := CyclicBarrier{}
	b.count = num
	b.parties = num
	b.condition = sync.NewCond(&sync.Mutex{})
	b.f = f
	return &b
}
