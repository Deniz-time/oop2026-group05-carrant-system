
package edu.aitu.oop3.repository.jdbc;

import edu.aitu.oop3.entity.Payment;
import edu.aitu.oop3.repository.PaymentRepository;

import java.sql.*;

public class JdbcPaymentRepository implements PaymentRepository {

    @Override
    public long create(Connection conn, Payment payment) throws SQLException {
        String sql = """
                INSERT INTO payments(rental_id, amount, paid_at, method)
                VALUES (?,?,?,?)
                RETURNING id
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, payment.getRentalId());
            ps.setInt(2, payment.getAmount());
            ps.setTimestamp(3, Timestamp.valueOf(payment.getPaidAt()));
            ps.setString(4, payment.getMethod());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }
}
