package me.eqxdev.afreeze.utils.inventory;

import org.apache.commons.collections4.map.HashedMap;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eqxDev on 01/04/2017.
 */
public class InventoryGenerator {

    public static Map<String, InventoryGenerator> inventories = new HashMap<>();

    public static Map<String, InventoryGenerator> getInventories() {
        return inventories;
    }

    public static void setInventorys(Map<String, InventoryGenerator> inventories) {
        InventoryGenerator.inventories = inventories;
    }

    private Inventory inv;


    public InventoryGenerator(String title, int rows) {
        inv = Bukkit.createInventory(null,rows*9,title);
    }

    public Inventory getInventory() {
        return inv;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    public void items(ItemStack is, int slot) {
        inv.setItem(slot,is);
    }
    public void items(InventoryItem item) {
        inv.setItem(item.getSlot(),item.generate());
    }
    public void items(List<InventoryItem> item) { item.forEach((inventoryItem -> inv.setItem(inventoryItem.getSlot(),inventoryItem.generate()))); }
}
