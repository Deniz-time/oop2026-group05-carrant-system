package edu.aitu.oop3.db;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final Properties PROPS = new Properties();

    static {
        // read db.properties from project root automatically
        try (InputStream in = Files.newInputStream(Path.of("db.properties"))) {
            PROPS.load(in);
        } catch (Exception ignored) {
        }
    }

    private DatabaseConnection() {}

    private static String val(String envKey, String propKey, String def) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) return env;
        String prop = PROPS.getProperty(propKey);
        if (prop != null && !prop.isBlank()) return prop;
        return def;
    }

    public static Connection getConnection() throws SQLException {
        String url = val("DB_URL", "db.url", "");
        String user = val("DB_USER", "db.user", "");
        String pass = val("DB_PASSWORD", "db.password", "");
        return DriverManager.getConnection(url, user, pass);
    }
}