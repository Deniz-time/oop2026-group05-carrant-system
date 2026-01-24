
package edu.aitu.oop3.repository.jdbc;
import edu.aitu.oop3.entity.RentalHistoryItem;
import java.util.ArrayList;
import java.util.List;
import edu.aitu.oop3.entity.Rental;
import edu.aitu.oop3.entity.RentalStatus;
import edu.aitu.oop3.repository.RentalRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class JdbcRentalRepository implements RentalRepository {

    @Override
    public Optional<Rental> findById(Connection conn, long id) throws SQLException {
        String sql = """
                SELECT id, car_id, customer_id, start_date, end_date, status, total_cost, created_at, closed_at
                FROM rentals
                WHERE id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public Optional<Rental> findOpenById(Connection conn, long id) throws SQLException {
        String sql = """
                SELECT id, car_id, customer_id, start_date, end_date, status, total_cost, created_at, closed_at
                FROM rentals
                WHERE id = ? AND status = 'OPEN'
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public boolean existsOverlap(Connection conn, long carId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
                SELECT 1
                FROM rentals r
                WHERE r.car_id = ?
                  AND r.status = 'OPEN'
                  AND NOT (r.end_date < ? OR r.start_date > ?)
                LIMIT 1
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, carId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public long create(Connection conn, Rental rental) throws SQLException {
        String sql = """
                INSERT INTO rentals(car_id, customer_id, start_date, end_date, status, created_at)
                VALUES (?,?,?,?, 'OPEN', NOW())
                RETURNING id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, rental.getCarId());
            ps.setLong(2, rental.getCustomerId());
            ps.setDate(3, Date.valueOf(rental.getStartDate()));
            ps.setDate(4, Date.valueOf(rental.getEndDate()));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    @Override
    public void close(Connection conn, long rentalId, int totalCost, LocalDateTime closedAt) throws SQLException {
        String sql = """
                UPDATE rentals
                SET status = 'CLOSED', total_cost = ?, closed_at = ?
                WHERE id = ? AND status = 'OPEN'
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, totalCost);
            ps.setTimestamp(2, Timestamp.valueOf(closedAt));
            ps.setLong(3, rentalId);
            ps.executeUpdate();
        }
    }
    private Rental map(ResultSet rs) throws SQLException {
        Timestamp created = rs.getTimestamp("created_at");
        Timestamp closed = rs.getTimestamp("closed_at");
        Integer total = (Integer) rs.getObject("total_cost");
        return new Rental(
                rs.getLong("id"),
                rs.getLong("car_id"),
                rs.getLong("customer_id"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                RentalStatus.valueOf(rs.getString("status")),
                total,
                created == null ? null : created.toLocalDateTime(),
                closed == null ? null : closed.toLocalDateTime()
        );
    }
    @Override
    public List<RentalHistoryItem> findHistoryAll(Connection conn) throws SQLException {
        String sql = """
        SELECT
          r.id AS rental_id,
          r.car_id,
          c.brand AS car_brand,
          c.model AS car_model,
          r.customer_id,
          cu.full_name AS customer_name,
          r.start_date,
          r.end_date,
          r.status,
          r.total_cost,
          r.created_at,
          r.closed_at
        FROM rentals r
        JOIN cars c ON c.id = r.car_id
        JOIN customers cu ON cu.id = r.customer_id
        ORDER BY r.id DESC
        """;
        List<RentalHistoryItem> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapHistory(rs));
            }
        }
        return list;
    }

    @Override
    public List<RentalHistoryItem> findHistoryByCustomerId(Connection conn, long customerId) throws SQLException {
        String sql = """
        SELECT
          r.id AS rental_id,
          r.car_id,
          c.brand AS car_brand,
          c.model AS car_model,
          r.customer_id,
          cu.full_name AS customer_name,
          r.start_date,
          r.end_date,
          r.status,
          r.total_cost,
          r.created_at,
          r.closed_at
        FROM rentals r
        JOIN cars c ON c.id = r.car_id
        JOIN customers cu ON cu.id = r.customer_id
        WHERE r.customer_id = ?
        ORDER BY r.id DESC
        """;
        List<RentalHistoryItem> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapHistory(rs));
                }
            }
        }
        return list;
    }

    private RentalHistoryItem mapHistory(ResultSet rs) throws SQLException {
        RentalHistoryItem it = new RentalHistoryItem();
        it.setRentalId(rs.getLong("rental_id"));

        it.setCarId(rs.getLong("car_id"));
        it.setCarBrand(rs.getString("car_brand"));
        it.setCarModel(rs.getString("car_model"));

        it.setCustomerId(rs.getLong("customer_id"));
        it.setCustomerName(rs.getString("customer_name"));

        it.setStartDate(rs.getDate("start_date").toLocalDate());
        it.setEndDate(rs.getDate("end_date").toLocalDate());

        it.setStatus(rs.getString("status"));

        Object tc = rs.getObject("total_cost");
        it.setTotalCost(tc == null ? null : ((Number) tc).intValue());

        Timestamp created = rs.getTimestamp("created_at");
        Timestamp closed = rs.getTimestamp("closed_at");
        it.setCreatedAt(created == null ? null : created.toLocalDateTime());
        it.setClosedAt(closed == null ? null : closed.toLocalDateTime());

        return it;
    }
}