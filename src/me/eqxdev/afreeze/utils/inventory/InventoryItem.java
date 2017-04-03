package me.eqxdev.afreeze.utils.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eqxDev on 01/04/2017.
 */
public class InventoryItem {

    private ItemStack is;
    private int slot;

    private String title;
    private List<String> lore;
    private String type;


    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack generate() {
        return is == null ? update() : is;
    }

    private ItemStack update() {
        if (is != null) {
            return is;
        }

        // generate
        refreshCache();
        return is;
    }

    public void refreshCache() {
        ItemStack item = null;
        if (type.contains(":")) {
            String[] types = type.split(":");
            item = new ItemStack(Material.getMaterial(types[0]), 1, (short) Integer.parseInt(types[1]));
        } else {
            item = new ItemStack(Material.getMaterial(type));
        }
        ItemMeta im = item.getItemMeta();
        if (title != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        }
        if (lore != null) {
            List<String> lores = new ArrayList<>();
            for (String loreItem : lore) {
                lores.add(ChatColor.translateAlternateColorCodes('&', loreItem));
            }
            im.setLore(lores);
        }
        item.setItemMeta(im);
        is = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
