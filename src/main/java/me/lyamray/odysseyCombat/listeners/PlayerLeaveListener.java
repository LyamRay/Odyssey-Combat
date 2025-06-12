package me.lyamray.odysseyCombat.listeners;

import lombok.Getter;
import lombok.Setter;
import me.lyamray.odysseyCombat.OdysseyCombat;
import me.lyamray.odysseyCombat.managers.Base64Manager;
import me.lyamray.odysseyCombat.managers.CitizensManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class PlayerLeaveListener implements Listener {

    private static final Map<UUID, ItemStack[]> armorContents = new HashMap<>();
    private static final Map<UUID, ItemStack[]> inventoryContents = new HashMap<>();

    @EventHandler
    public void playerLeft(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (isInCombat(player)) return;

        handleContentSave(player);

        ItemStack[] armorContent = armorContents.get(uuid);
        ItemStack[] inventoryContent = inventoryContents.get(uuid);

        new CitizensManager().spawnNPC(player, inventoryContent, armorContent);
    }

    private void handleContentSave(Player player) throws SQLException {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation();

        ItemStack[] armorContent = player.getInventory().getArmorContents();
        ItemStack[] inventoryContent = player.getInventory().getContents();

        armorContents.computeIfAbsent(uuid, k -> armorContent);
        inventoryContents.computeIfAbsent(uuid, k -> inventoryContent);

        String[] encoded = Base64Manager.playerInventoryEncode(player.getInventory());

        String encodedInventoryContents = encoded[0];
        String encodedArmorContents = encoded[1];

        OdysseyCombat.getDatabase().setLastLocation(uuid, location);
        OdysseyCombat.getDatabase().setInventoryContents(uuid, encodedInventoryContents);
        OdysseyCombat.getDatabase().setArmorContents(uuid, encodedArmorContents);
    }

    private boolean isInCombat(Player player) throws SQLException {
        return !OdysseyCombat.getDatabase().isPlayerCombatTagged(player.getUniqueId());
    }
}
