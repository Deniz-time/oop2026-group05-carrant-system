
package edu.aitu.oop3.repository.jdbc;

import edu.aitu.oop3.entity.Car;
import edu.aitu.oop3.repository.CarRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCarRepository implements CarRepository {

    @Override
    public Optional<Car> findById(Connection conn, long id) throws SQLException {
        String sql = "SELECT id, brand, model, daily_price, is_available FROM cars WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public List<Car> findAvailable(Connection conn, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
                SELECT c.id, c.brand, c.model, c.daily_price, c.is_available
                FROM cars c
                WHERE c.is_available = TRUE
                  AND NOT EXISTS (
                    SELECT 1 FROM rentals r
                    WHERE r.car_id = c.id
                      AND r.status = 'OPEN'
                      AND NOT (r.end_date < ? OR r.start_date > ?)
                  )
                ORDER BY c.id
                """;
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cars.add(map(rs));
                }
            }
        }
        return cars;
    }

    @Override
    public void setAvailability(Connection conn, long carId, boolean available) throws SQLException {
        String sql = "UPDATE cars SET is_available = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setLong(2, carId);
            ps.executeUpdate();
        }
    }

    @Override
    public long create(Connection conn, Car car) throws SQLException {
        String sql = "INSERT INTO cars(brand, model, daily_price, is_available) VALUES (?,?,?,?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getDailyPrice());
            ps.setBoolean(4, car.isAvailable());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private Car map(ResultSet rs) throws SQLException {
        return new Car(
                rs.getLong("id"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getInt("daily_price"),
                rs.getBoolean("is_available")
        );
    }
}