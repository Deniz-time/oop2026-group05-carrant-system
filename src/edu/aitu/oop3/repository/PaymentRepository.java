
package edu.aitu.oop3.repository;

import edu.aitu.oop3.entity.Payment;

import java.sql.Connection;
import java.sql.SQLException;

public interface PaymentRepository {
    long create(Connection conn, Payment payment) throws SQLException;
}
