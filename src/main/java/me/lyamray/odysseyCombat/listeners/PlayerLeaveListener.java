package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.managers.Base64Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void playerLeft(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (isInCombat(player)) return;

        handleContentSave(player);
    }

    private void handleContentSave(Player player) throws SQLException {
        UUID uuid = player.getUniqueId();

        String[] encoded = Base64Manager.playerInventoryEncode(player.getInventory());

        String encodedInventoryContents = encoded[0];
        String encodedArmorContents = encoded[1];

        OdysseyCombat.getDatabase().setInventoryContents(uuid, encodedInventoryContents);
        OdysseyCombat.getDatabase().setArmorContents(uuid, encodedArmorContents);
    }

    private boolean isInCombat(Player player) throws SQLException {
        return !OdysseyCombat.getDatabase().isPlayerCombatTagged(player.getUniqueId());
    }
}
