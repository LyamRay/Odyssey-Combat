package me.lyamray.odysseyCombat.managers;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class LootTableManager {


    public void applyItemStackLoot(ItemStack[] sourceItems, Inventory inventory) {
        List<ItemStack> lootableItems = new ArrayList<>();

        for (ItemStack item : sourceItems) {
            if (item != null && !item.getType().isAir()) {
                lootableItems.add(item.clone());
            }
        }

        Collections.shuffle(lootableItems);

        for (int i = 0; i < Math.min(inventory.getSize(), lootableItems.size()); i++) {
            inventory.setItem(i, lootableItems.get(i));
        }
    }
}
