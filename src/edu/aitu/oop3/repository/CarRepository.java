
package edu.aitu.oop3.repository;

import edu.aitu.oop3.entity.Car;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Optional<Car> findById(Connection conn, long id) throws SQLException;
    List<Car> findAvailable(Connection conn, LocalDate from, LocalDate to) throws SQLException;
    void setAvailability(Connection conn, long carId, boolean available) throws SQLException;
    long create(Connection conn, Car car) throws SQLException;
}
