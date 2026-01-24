
package edu.aitu.oop3.service.impl;

import edu.aitu.oop3.entity.Car;
import edu.aitu.oop3.service.PricingService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DefaultPricingService implements PricingService {

    @Override
    public int calculateTotalCost(Car car, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (days <= 0) throw new IllegalArgumentException("Invalid dates");
        long total = days * (long) car.getDailyPrice();
        if (total > Integer.MAX_VALUE) throw new IllegalArgumentException("Too large total");
        return (int) total;
    }
}