package me.lyamray.odysseyCombat.utils;

import me.lyamray.odysseyCombat.OdysseyCombat;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarTimer {

    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> timers = new HashMap<>();

    public void startTimer(Player player, int durationSeconds) {
        UUID uuid = player.getUniqueId();

        if (timers.containsKey(uuid)) {
            timers.get(uuid).cancel();
        }

        BossBar bar = bossBars.computeIfAbsent(uuid, id -> {
            BossBar newBar = BossBar.bossBar(
                    ChatUtils.color("<gradient:#ff0000:#df6d78>In Combat - Log niet uit!</gradient>"),
                    1.0f,
                    BossBar.Color.RED,
                    BossBar.Overlay.PROGRESS
            );
            player.showBossBar(newBar);
            return newBar;
        });

        final int totalTicks = durationSeconds * 20;

        BukkitRunnable task = new BukkitRunnable() {
            int ticksLeft = totalTicks;

            @Override
            public void run() {
                if (!player.isOnline() || ticksLeft <= 0) {
                    player.hideBossBar(bar);
                    bossBars.remove(uuid);
                    timers.remove(uuid);
                    cancel();
                    return;
                }

                float progress = (float) ticksLeft / totalTicks;
                bar.progress(progress);
                ticksLeft--;
            }
        };

        task.runTaskTimer(OdysseyCombat.getInstance(), 0L, 1L);
        timers.put(uuid, task);
    }

    public void updateTimer(Player player, int durationSeconds) {
        startTimer(player, durationSeconds);
    }

    public void cancelTimer(Player player) {
        UUID uuid = player.getUniqueId();
        if (timers.containsKey(uuid)) {
            timers.get(uuid).cancel();
            timers.remove(uuid);
        }
        if (bossBars.containsKey(uuid)) {
            player.hideBossBar(bossBars.get(uuid));
            bossBars.remove(uuid);
        }
    }
}
