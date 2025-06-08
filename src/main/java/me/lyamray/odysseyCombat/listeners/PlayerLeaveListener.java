package me.lyamray.odysseyCombat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void PlayerLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();

    }
}
