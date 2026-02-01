package edu.aitu.oop3.repository;

import edu.aitu.oop3.entity.Car;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface CarRepository extends Repository<Car> {
    List<Car> findAvailableForDates(Connection conn, LocalDate from, LocalDate to) throws SQLException;
    void setAvailability(Connection conn, long carId, boolean available) throws SQLException;
}