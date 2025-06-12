package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.database.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void PlayerJoined(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        //if (!player.hasPlayedBefore()) {
            lookInDatabase(player);
        //}
        setCombatTaggedFalse(player);
    }

    private void lookInDatabase(Player player) throws SQLException {
        Database database = OdysseyCombat.getDatabase();
        if (database.existsPlayer(player.getUniqueId())) return;
        database.addPlayer(player.getUniqueId());
    }

    private void setCombatTaggedFalse(Player victim) throws SQLException {
        Database database = OdysseyCombat.getDatabase();
        database.setPlayerCombattagged(victim.getUniqueId(), false);
    }
}
