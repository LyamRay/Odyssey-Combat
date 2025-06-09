package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.utils.BossBarTimer;
import me.lyamray.odysseyCombat.utils.CombatMessages;
import net.kyori.adventure.text.Component;
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

        new BossBarTimer().startTimer(victim.getPlayer(), 20);
        new BossBarTimer().startTimer(attacker.getPlayer(), 20);

        Component attackerComponent = CombatMessages.combatHitMessage(victim.getName());
        Component victimComponent = CombatMessages.combatGetHitMessage(victim.getName());

        attacker.sendMessage(attackerComponent);
        victim.sendMessage(victimComponent);
    }
}
