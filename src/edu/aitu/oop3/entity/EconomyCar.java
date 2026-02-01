package edu.aitu.oop3.entity;

public class EconomyCar extends Car {
    public EconomyCar(Long id, String brand, String model, int dailyPrice, boolean available) {
        super(id, brand, model, dailyPrice, available, CarType.ECONOMY);
    }
}
