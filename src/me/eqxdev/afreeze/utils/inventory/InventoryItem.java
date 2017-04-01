package me.eqxdev.afreeze.utils.inventory;

import org.bukkit.inventory.ItemStack;

/**
 * Created by eqxDev on 01/04/2017.
 */
public class InventoryItem {

    private ItemStack is;
    private int slot;


    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack generate() {
        return is==null?update():is;
    }

    private ItemStack update() {
        if(is != null) {
            return is;
        }



        return is;
    }
}
