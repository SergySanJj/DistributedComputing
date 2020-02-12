package com.sergeiyarema;

public class Config {
    public static class Customer {
        public static int hairstyleTime = 100;
        public static int generationInterval = 10;
        public static int generationSigma = 100;

        static {
            generationSigma = generationInterval + generationSigma;
        }
    }

    public static class Main {
        public static int workTime = 5000;
    }

    public static class Barbershop {
        public static int queueCapacity = 10;
    }
}
