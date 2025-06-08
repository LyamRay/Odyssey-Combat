package me.lyamray.odysseyCombat.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitListener implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        Entity damager = event.getDamager();

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            damager = shooter;
        }

        if (!(damager instanceof Player attacker)) return;

        attacker.sendMessage("You hit " + victim.getName() + "!");
        victim.sendMessage("You were hit by " + attacker.getName() + "!");
    }
}
