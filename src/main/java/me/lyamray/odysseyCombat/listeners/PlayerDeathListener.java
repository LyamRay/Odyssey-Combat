package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.utils.BossBarTimer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void playerDied (@NotNull PlayerDeathEvent event) throws SQLException {
        Player player = event.getPlayer();

        if (isInCombat(player)) return;
        player.sendMessage("Je bent in combat dood gegaan! Je bent uit combat nu. //DEBUG");
        BossBarTimer.getInstance().cancelTimer(player);

    }

    private boolean isInCombat(Player player) throws SQLException {
        return !OdysseyCombat.getDatabase().isPlayerCombatTagged(player.getUniqueId());
    }
}
