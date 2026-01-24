
package edu.aitu.oop3.repository;

import edu.aitu.oop3.entity.Rental;
import edu.aitu.oop3.entity.RentalHistoryItem;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface RentalRepository {
    List<RentalHistoryItem> findHistoryAll(Connection conn) throws SQLException;
    List<RentalHistoryItem> findHistoryByCustomerId(Connection conn, long customerId) throws SQLException;
    Optional<Rental> findById(Connection conn, long id) throws SQLException;
    Optional<Rental> findOpenById(Connection conn, long id) throws SQLException;
    boolean existsOverlap(Connection conn, long carId, LocalDate from, LocalDate to) throws SQLException;
    long create(Connection conn, Rental rental) throws SQLException;
    void close(Connection conn, long rentalId, int totalCost, LocalDateTime closedAt) throws SQLException;

}
