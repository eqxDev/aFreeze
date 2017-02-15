package me.eqxdev.afreeze.listeners;

import me.eqxdev.afreeze.Main;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.Lang;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Created by eqxDev on 03/02/2017.
 */
public class FreezeHandler implements Listener {


    // Block Events
    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (FreezeManager.get().isFrozen(p)) {
            e.setCancelled(true);
            // Cant build while frozen
            Lang.FROZEN_BUILD.send(p);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (FreezeManager.get().isFrozen(p)) {
            e.setCancelled(true);
            // Cant build while frozen
            Lang.FROZEN_BUILD.send(p);
        }
    }

    @EventHandler
    public void interaction(PlayerInteractEvent e) {
        if (FreezeManager.get().isFrozen(e.getPlayer())) {
            Lang.FROZEN_BUILD.send(e.getPlayer());
            e.setCancelled(true);
        }
    }

    // Damage Events
    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e) {
        Boolean cancel = false;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (FreezeManager.get().isFrozen(p)) cancel = true;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (FreezeManager.get().isFrozen(p)) cancel = true;
        }
        if (cancel) {
            e.setDamage(0.0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockDamage(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (FreezeManager.get().isFrozen(p)) {
                e.setDamage(0.0);
                e.setCancelled(true);
            }
        }
    }

    // Item Events
    @EventHandler
    public void pickupItem(PlayerPickupItemEvent e) {
        if (FreezeManager.get().isFrozen(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e) {
        if (FreezeManager.get().isFrozen(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    // Move Events
    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (FreezeManager.get().isFrozen(p)) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            if(!(p.getVelocity().getY() < -0.0784000015258788 && p.getVelocity().getY() > -0.0784000015258790)) {
                to.setY(e.getTo().getY());
            }
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if(FreezeManager.get().isFrozen(e.getWhoClicked().getUniqueId())) {
            if(e.getInventory().getTitle().equals(Main.get().titleInv)) {
                e.setCancelled(true);
            }
        }
    }

}
