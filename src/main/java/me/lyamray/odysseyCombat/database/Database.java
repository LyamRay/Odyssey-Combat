package me.lyamray.odysseyCombat.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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
                            armorContents TEXT,
                            lastLocation TEXT,
                            combatTagged BOOLEAN NOT NULL DEFAULT 0
                        );
                    """);
        }
    }

    public boolean existsPlayer(UUID uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT uuid FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void addPlayer(UUID uuid) throws SQLException {
        if (!existsPlayer(uuid)) {
            try (PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO players (uuid, contents, armorContents, lastLocation, combatTagged)
                        VALUES (?, ?, ?, ?, 0);
                    """)) {
                statement.setString(1, uuid.toString());
                statement.executeUpdate();
            }
        }
    }

    public void setPlayerCombattagged(UUID uuid, boolean combattagged) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE players SET combatTagged = ? WHERE uuid = ?")) {
            statement.setBoolean(1, combattagged);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
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

    public void setInventoryContents(UUID uuid, String base64) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE players SET contents = ? WHERE uuid = ?")) {
            statement.setString(1, base64);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        }
    }

    public void setArmorContents(UUID uuid, String base64) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE players SET armorContents = ? WHERE uuid = ?")) {
            statement.setString(1, base64);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        }
    }

    public Location getLastLocation(UUID uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT lastLocation FROM players WHERE uuid = ?")) {
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return parseLocation(resultSet.getString("lastLocation"));
                }
            }
        }
        return null;
    }

    public void setLastLocation(UUID uuid, Location location) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE players SET lastLocation = ? WHERE uuid = ?")) {
            statement.setString(1, location == null ? null : serializeLocation(location));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    private String serializeLocation(Location location) {
        if (location == null || location.getWorld() == null) return null;
        return String.format("%s,%d,%d,%d",
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ());
    }

    public Location parseLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) return null;
        try {
            String[] parts = locationString.split(",");
            World world = Bukkit.getWorld(parts[0]);
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            return new Location(world, x, y, z);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}