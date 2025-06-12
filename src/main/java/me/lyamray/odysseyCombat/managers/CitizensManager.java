package me.lyamray.odysseyCombat.managers;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Inventory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CitizensManager {

    public void spawnNPC(Player player, ItemStack[] inventoryContent, ItemStack[] armorContent) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, player.getName());
        LivingEntity livingEntity = (LivingEntity) npc.getEntity();
        livingEntity.setHealth(20);
        livingEntity.getEquipment().setArmorContents(armorContent);
        npc.getOrAddTrait(Inventory.class).setContents(inventoryContent);
        npc.getEntity().setInvulnerable(false);
        npc.setProtected(false);
    }
}
