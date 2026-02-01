package edu.aitu.oop3.entity;

public class SuvCar extends Car {
    public SuvCar(Long id, String brand, String model, int dailyPrice, boolean available) {
        super(id, brand, model, dailyPrice, available, CarType.SUV);
    }
}