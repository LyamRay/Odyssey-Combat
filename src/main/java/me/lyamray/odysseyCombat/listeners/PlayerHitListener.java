package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.database.Database;
import me.lyamray.odysseyCombat.utils.BossBarTimer;
import me.lyamray.odysseyCombat.utils.CombatMessages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.sql.SQLException;


public class PlayerHitListener implements Listener {

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) throws SQLException {
        if (!(event.getEntity() instanceof Player victim)) return;

        Entity damager = event.getDamager();

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            damager = shooter;
        }

        if (!(damager instanceof Player attacker)) return;

        isAlreadyInCombat(victim, attacker);
    }

    private void isAlreadyInCombat(Player victim, Player attacker) throws SQLException {
        BossBarTimer bossBarTimer = new BossBarTimer();
        Database database = OdysseyCombat.getDatabase();

        boolean victimTagged = database.isPlayerCombatTagged(victim.getUniqueId());
        boolean attackerTagged = database.isPlayerCombatTagged(attacker.getUniqueId());

        if (victimTagged || attackerTagged) {
            victim.sendMessage("Je bent al in combat, geen extra message maar wel de bossbar die terug vol wordt! //debug");
            bossBarTimer.updateTimer(victim, 20);
            bossBarTimer.updateTimer(attacker, 20);

        } else {

            CombatMessages.combatGetHitMessage(attacker.getName(), victim);
            CombatMessages.combatHitMessage(victim.getName(), attacker);

            bossBarTimer.startTimer(victim, 20);
            bossBarTimer.startTimer(attacker, 20);
        }
    }
}
