package me.lyamray.odysseyCombat.managers;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Inventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CitizensManager {

    public void spawnNPC(Player player, ItemStack[] inventoryContent, ItemStack[] armorContent) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, player.getName());
        npc.spawn(player.getLocation());
        LivingEntity livingEntity = (LivingEntity) npc.getEntity();
        livingEntity.setHealth(20);
        livingEntity.getEquipment().setArmorContents(armorContent);
        npc.getOrAddTrait(Inventory.class).setContents(inventoryContent);
        npc.getEntity().setInvulnerable(false);
        npc.setProtected(false);
    }
}
