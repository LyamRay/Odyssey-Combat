package me.lyamray.odysseyCombat;

import lombok.Getter;
import me.lyamray.odysseyCombat.database.Database;
import me.lyamray.odysseyCombat.listeners.PlayerJoinListener;
import me.lyamray.odysseyCombat.listeners.PlayerLeaveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Arrays;

public final class OdysseyCombat extends JavaPlugin {

    @Getter
    private static OdysseyCombat instance;

    @Getter
    private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        checkFolderAndConnect();
        registerListener();
        getLogger().info("The plugin has started successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            database.closeConnection();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to disable the database properly! " + exception);
        }
        getLogger().finest("The plugin has shutdown successfully!");
    }

    public void checkFolderAndConnect() {
        try {
            if (!getDataFolder().exists()) {
                if (!getDataFolder().mkdirs()) {
                    System.err.println("Failed to create the data folder!");
                    Bukkit.getPluginManager().disablePlugin(this);
                    return;
                }
            }

            database = new Database(getDataFolder().getAbsolutePath() + "/OdysseyCombat.db");
            getLogger().info("Connected successfully to the database!");
        } catch (SQLException exception) {
            Bukkit.getPluginManager().disablePlugin(this);
            throw new RuntimeException("Failed to connect to the database. " + exception);
        }
    }

    public void registerListener() {
        Arrays.asList(
                new PlayerJoinListener(),
                new PlayerLeaveListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
