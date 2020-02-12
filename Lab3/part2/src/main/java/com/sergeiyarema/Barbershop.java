package com.sergeiyarema;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Barbershop {
    private Queue<Customer> queue = new LinkedList<>();
    private final Object chair = new Object();
    private Customer currentCustomer = null;

    private final Object barberNotifier = new Object();

    public void standInQueue(Customer customer) {
        queue.add(customer);
    }

    public boolean isEmpty() {
        synchronized (chair) {
            return queue.isEmpty();
        }
    }

    public Customer getNextCustomer() {
        synchronized (chair) {
//            System.out.println("Getting next customer");
            if (currentCustomer != null) {
                return currentCustomer;
            } else if (!isEmpty()) {
                return queue.remove();
            }
            return null;
        }
    }

    public boolean isBarberWorking() {
        return currentCustomer != null;
    }

    public Object getBarberNotifier() {
        synchronized (barberNotifier) {
            return barberNotifier;
        }
    }

    public void wakeUpBarber() {
        barberNotifier.notify();
    }


    public void trySitInChair(Customer customer) {
        synchronized (chair) {
            if (currentCustomer != null) {
                System.out.println("Customer stands in line");
                queue.add(customer);
            } else {
                System.out.println("Sitting in chair");
                currentCustomer = customer;
            }
        }
    }


    public void freeChair() {
        synchronized (chair) {
            currentCustomer = null;
        }
    }


}
