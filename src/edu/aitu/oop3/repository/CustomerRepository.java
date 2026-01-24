
package edu.aitu.oop3.repository;

import edu.aitu.oop3.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Connection conn, long id) throws SQLException;
    long create(Connection conn, Customer customer) throws SQLException;
}
