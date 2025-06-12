package me.lyamray.odysseyCombat.listeners;

import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.database.Database;
import me.lyamray.odysseyCombat.managers.Base64Manager;
import me.lyamray.odysseyCombat.utils.BossBarTimer;
import me.lyamray.odysseyCombat.utils.CombatMessages;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerHitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHit(EntityDamageByEntityEvent event) throws SQLException {
        if (!(event.getEntity() instanceof Player victim)) return;

        Entity damager = event.getDamager();

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player shooter) {
            damager = shooter;
        }

        if (!(damager instanceof Player attacker)) return;

        cantDo(victim, attacker, event);

    }

    private void setCombatTaggedTrue(Player victim, Player attacker) throws SQLException {
        Database database = OdysseyCombat.getDatabase();
        database.setPlayerCombattagged(victim.getUniqueId(), true);
        database.setPlayerCombattagged(attacker.getUniqueId(), true);
    }

    private void handleInCombat(Player victim, Player attacker) throws SQLException {
        Database database = OdysseyCombat.getDatabase();
        BossBarTimer bossBarTimer = BossBarTimer.getInstance();

        if (isInCombat(attacker)) {
            CombatMessages.combatHitMessage(victim.getName(), attacker);
        }

        if (isInCombat(victim)) {
            CombatMessages.combatGetHitMessage(attacker.getName(), victim);
        }

        handlePlayerCombatTag(victim, database, bossBarTimer);
        handlePlayerCombatTag(attacker, database, bossBarTimer);

        setCombatTaggedTrue(victim, attacker);

    }

    private void cantDo(Player victim, Player attacker, EntityDamageByEntityEvent event) throws SQLException {
        if (!(victim.getGameMode() == GameMode.SURVIVAL) || !(attacker.getGameMode() == GameMode.SURVIVAL)) return;

        if (event.isCancelled() || event.getFinalDamage() <= 0) return;

        handleInCombat(victim, attacker);
    }

    private void handlePlayerCombatTag(Player player, Database database, BossBarTimer bossBarTimer) throws SQLException {
        UUID uuid = player.getUniqueId();
        boolean isInCombat = database.isPlayerCombatTagged(uuid);

        if (!isInCombat) {
            database.setPlayerCombattagged(uuid, true);
            bossBarTimer.startTimer(player, 20);

        } else {
            bossBarTimer.updateTimer(player, 20);
        }
    }


    private boolean isInCombat(Player player) throws SQLException {
        return !OdysseyCombat.getDatabase().isPlayerCombatTagged(player.getUniqueId());
    }
}
