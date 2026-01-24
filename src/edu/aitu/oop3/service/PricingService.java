
package edu.aitu.oop3.service;

import edu.aitu.oop3.entity.Car;

import java.time.LocalDate;

public interface PricingService {
    int calculateTotalCost(Car car, LocalDate startDate, LocalDate endDate);
}
