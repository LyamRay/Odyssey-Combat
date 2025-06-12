package me.lyamray.odysseyCombat.utils;

import lombok.Getter;
import me.lyamray.odysseyCombat.OdysseyCombat;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarTimer {

    @Getter
    private static final BossBarTimer instance = new BossBarTimer();

    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> timers = new HashMap<>();

    private BossBarTimer() {
    }

    public void startTimer(Player player, int durationSeconds) throws SQLException {
        UUID uuid = player.getUniqueId();

        if (timers.containsKey(uuid)) {
            timers.get(uuid).cancel();
            timers.remove(uuid);
            player.hideBossBar(bossBars.get(uuid));
            bossBars.remove(uuid);
        }

        createBossBar(player, durationSeconds);
    }

    private void createBossBar(Player player, int durationSeconds) {
        UUID uuid = player.getUniqueId();

        BossBar bar = BossBar.bossBar(
                ChatUtils.color("<gradient:#ff0000:#df6d78>In Combat - Log niet uit!</gradient>"),
                1.0f,
                BossBar.Color.RED,
                BossBar.Overlay.PROGRESS
        );

        bossBars.put(uuid, bar);
        player.showBossBar(bar);

        BukkitRunnable task = getBukkitRunnable(player, bar, uuid, durationSeconds);
        timers.put(uuid, task);
        task.runTaskTimer(OdysseyCombat.getInstance(), 0L, 1L);
    }

    private @NotNull BukkitRunnable getBukkitRunnable(Player player, BossBar bar, UUID uuid, int durationSeconds) {
        final int totalTicks = durationSeconds * 20;

        return new BukkitRunnable() {
            int ticksLeft = totalTicks;

            @Override
            public void run() {
                if (!player.isOnline() || ticksLeft <= 0) {
                    player.hideBossBar(bar);
                    bossBars.remove(uuid);
                    timers.remove(uuid);
                    try {
                        setCombatTaggedFalse(player);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    cancel();
                    return;
                }

                float progress = (float) ticksLeft / totalTicks;
                bar.progress(progress);
                ticksLeft--;
            }
        };
    }

    private void setCombatTaggedFalse(Player player) throws SQLException {
        player.sendMessage("Je bent uit combat! //debug");
        OdysseyCombat.getDatabase().setPlayerCombattagged(player.getUniqueId(), false);
    }

    public void updateTimer(Player player, int durationSeconds) throws SQLException {
        if (isInCombat(player)) return;

        UUID uuid = player.getUniqueId();
        if (timers.containsKey(uuid)) {
            BossBar bar = bossBars.get(uuid);
            if (bar != null) {
                bar.progress(1.0f);
            }

            timers.get(uuid).cancel();
            BukkitRunnable newTask = getBukkitRunnable(player, bossBars.get(uuid), uuid, durationSeconds);
            timers.put(uuid, newTask);
            newTask.runTaskTimer(OdysseyCombat.getInstance(), 0L, 1L);
        } else {
            startTimer(player, durationSeconds);
        }
    }

    public void cancelTimer(Player player) throws SQLException {
        UUID uuid = player.getUniqueId();
        BossBar bar = bossBars.get(uuid);

        if (isInCombat(player)) return;
        if (!timers.containsKey(uuid)) return;
        if (!bossBars.containsKey(uuid)) return;

        player.hideBossBar(bar);

        timers.remove(uuid);
        bossBars.remove(uuid);
    }

    private boolean isInCombat(Player player) throws SQLException {
        return !OdysseyCombat.getDatabase().isPlayerCombatTagged(player.getUniqueId());
    }
}
