
package edu.aitu.oop3.repository.jdbc;

import edu.aitu.oop3.entity.Customer;
import edu.aitu.oop3.repository.CustomerRepository;

import java.sql.*;
import java.util.Optional;

public class JdbcCustomerRepository implements CustomerRepository {

    @Override
    public Optional<Customer> findById(Connection conn, long id) throws SQLException {
        String sql = "SELECT id, full_name, age, driver_license_number FROM customers WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        }
    }

    @Override
    public java.util.List<Customer> findAll(Connection conn) throws SQLException {
        String sql = "SELECT id, full_name, age, driver_license_number FROM customers ORDER BY id";
        java.util.List<Customer> list = new java.util.ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public long create(Connection conn, Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(full_name, age, driver_license_number) VALUES (?,?,?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFullName());
            ps.setInt(2, customer.getAge());
            ps.setString(3, customer.getDriverLicenseNumber());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("full_name"),
                rs.getInt("age"),
                rs.getString("driver_license_number")
        );
    }
}