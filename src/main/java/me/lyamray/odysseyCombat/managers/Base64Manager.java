package me.lyamray.odysseyCombat.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Base64Manager {

    private static final Gson gson = new Gson();
    private static final Type ITEM_LIST_TYPE = new TypeToken<List<Map<String, Object>>>() {}.getType();

    public String[] playerInventoryEncode(PlayerInventory inventory) {
        String content = toBase64Inventory(inventory);
        String armor = toBase64Armor(inventory.getArmorContents());
        return new String[]{content, armor};
    }

    private String toBase64Inventory(Inventory inventory) {
        List<Map<String, Object>> serializedItems = new ArrayList<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            serializedItems.add(item != null ? item.serialize() : null);
        }

        String json = gson.toJson(serializedItems);
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    private String toBase64Armor(ItemStack[] armor) {
        List<Map<String, Object>> serializedArmor = new ArrayList<>();
        for (ItemStack item : armor) {
            serializedArmor.add(item != null ? item.serialize() : null);
        }

        String json = gson.toJson(serializedArmor);
        return Base64.getEncoder().encodeToString(json.getBytes());
    }

    public ItemStack[] fromBase64ToContents(String base64, int size) {
        String json = new String(Base64.getDecoder().decode(base64));
        List<Map<String, Object>> itemMaps = gson.fromJson(json, ITEM_LIST_TYPE);

        ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size && i < itemMaps.size(); i++) {
            Map<String, Object> itemMap = itemMaps.get(i);
            items[i] = itemMap != null ? ItemStack.deserialize(itemMap) : null;
        }
        return items;
    }
}