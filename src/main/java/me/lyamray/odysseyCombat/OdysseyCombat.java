package me.lyamray.odysseyCombat;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class OdysseyCombat extends JavaPlugin {

    @Getter
    private static OdysseyCombat instace;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instace = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
