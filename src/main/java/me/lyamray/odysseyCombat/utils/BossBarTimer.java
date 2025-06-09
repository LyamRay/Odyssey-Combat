package me.lyamray.odysseyCombat.utils;

import me.lyamray.odysseyCombat.OdysseyCombat;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarTimer {

    public void startTimer(Player player, int durationSeconds) {

        BossBar bar = BossBar.bossBar(
                ChatUtils.color("<gradient:#ff0000:#df6d78>In Combat - Log niet uit!</gradient>"),
                1.0f,
                BossBar.Color.RED,
                BossBar.Overlay.PROGRESS
        );

        player.showBossBar(bar);

        new BukkitRunnable() {
            final int totalTicks = durationSeconds * 20;
            final int[] ticksLeft = { totalTicks };

            @Override
            public void run() {
                if (!player.isOnline() || ticksLeft[0] <= 0) {
                    player.hideBossBar(bar);
                    cancel();
                    return;
                }

                float progress = (float) ticksLeft[0] / totalTicks;
                bar.progress(progress);
                ticksLeft[0]--;
            }
        }.runTaskTimer(OdysseyCombat.getInstance(), 0L, 1L);
    }
}
