package me.lyamray.odysseyCombat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS players (
                            uuid TEXT PRIMARY KEY,
                            bees INTEGER NOT NULL DEFAULT 0,
                            honey INTEGER NOT NULL DEFAULT 0,
                            bees_available INTEGER NOT NULL DEFAULT 1,
                            honey_available INTEGER NOT NULL DEFAULT 5,
                            field_level INTEGER NOT NULL DEFAULT 1,
                            hive_location TEXT
                        );
                    """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}