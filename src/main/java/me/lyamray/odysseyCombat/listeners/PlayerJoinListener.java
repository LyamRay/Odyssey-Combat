package me.lyamray.odysseyCombat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void PlayerJoined(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }
}
