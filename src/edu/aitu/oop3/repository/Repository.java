package edu.aitu.oop3.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    Optional<T> findById(Connection conn, long id) throws SQLException;
    List<T> findAll(Connection conn) throws SQLException;
    long create(Connection conn, T entity) throws SQLException;
}