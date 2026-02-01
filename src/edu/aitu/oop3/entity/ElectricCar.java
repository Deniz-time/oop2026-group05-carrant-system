package edu.aitu.oop3.entity;

public class ElectricCar extends Car {
    public ElectricCar(Long id, String brand, String model, int dailyPrice, boolean available) {
        super(id, brand, model, dailyPrice, available, CarType.ELECTRIC);
    }
}