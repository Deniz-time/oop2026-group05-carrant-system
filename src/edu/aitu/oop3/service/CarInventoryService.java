
package edu.aitu.oop3.service;

import edu.aitu.oop3.entity.Car;

import java.time.LocalDate;
import java.util.List;

public interface CarInventoryService {
    List<Car> searchAvailableCars(LocalDate from, LocalDate to);
}
