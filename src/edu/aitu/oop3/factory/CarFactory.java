package edu.aitu.oop3.factory;

import edu.aitu.oop3.entity.*;

public final class CarFactory {
    private CarFactory() {}

    public static Car create(CarType type, Long id, String brand, String model, int dailyPrice, boolean available) {
        if (type == null) type = CarType.ECONOMY;
        switch (type) {
            case SUV: return new SuvCar(id, brand, model, dailyPrice, available);
            case ELECTRIC: return new ElectricCar(id, brand, model, dailyPrice, available);
            default: return new EconomyCar(id, brand, model, dailyPrice, available);
        }
    }

    public static Car createNew(CarType type, String brand, String model, int dailyPrice) {
        return create(type, null, brand, model, dailyPrice, true);
    }
}