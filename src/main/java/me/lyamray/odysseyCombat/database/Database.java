package me.lyamray.odysseyCombat.database;

import java.sql.*;
import java.util.UUID;

public class Database {

    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS players (
                            uuid TEXT PRIMARY KEY,
                            contents TEXT,
                            combatTagged BOOLEAN NOT NULL DEFAULT 0
                        );
                    """);
        }
    }



    public boolean isPlayerCombatTagged(UUID uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT combatTagged FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("combatTagged");
                }
            }
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}