
package edu.aitu.oop3.service.impl;

import edu.aitu.oop3.entity.Car;
import edu.aitu.oop3.repository.CarRepository;
import edu.aitu.oop3.service.CarInventoryService;
import edu.aitu.oop3.db.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class DefaultCarInventoryService implements CarInventoryService {

    private final CarRepository carRepository;

    public DefaultCarInventoryService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> searchAvailableCars(LocalDate from, LocalDate to) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return carRepository.findAvailableForDates(conn, from, to);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}