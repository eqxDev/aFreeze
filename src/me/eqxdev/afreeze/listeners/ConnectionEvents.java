package me.eqxdev.afreeze.listeners;

import me.eqxdev.afreeze.Main;
import me.eqxdev.afreeze.utils.BukkitUtils;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.FreezeType;
import me.eqxdev.afreeze.utils.Lang;
import me.eqxdev.afreeze.utils.redglass.BarrierManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eqxDev on 13/02/2017.
 */
public class ConnectionEvents implements Listener {

    private List<UUID> users = new ArrayList<>();

    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        sendMessage("j", e.getPlayer());
        if (FreezeManager.get().isFreezeAll()) {
            if (!FreezeManager.get().isFrozen(e.getPlayer())) {
                FreezeManager.get().add(e.getPlayer(), FreezeType.ALL);
            }
        }
        if ((e.getPlayer().isOp() || e.getPlayer().hasPermission(Lang.PERM_UPDATE.toString())) && Main.NEW_UPDATE && !users.contains(e.getPlayer().getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getPlayer() != null) {
                        e.getPlayer().sendMessage(Lang.NEW_UPDATE.toString().replace("%version%", Main.NEW_UPDATE_VER));
                        users.add(e.getPlayer().getUniqueId());
                    }
                }
            }.runTaskLater(Main.get(), 20 * 5);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (FreezeManager.get().isFreezeAll()) {
            if (FreezeManager.get().isFrozen(e.getPlayer())) {
                FreezeManager.get().remove(e.getPlayer());
            }
        } else {
            sendMessage("l", e.getPlayer());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (!FreezeManager.get().isFreezeAll()) {
            if (FreezeManager.get().isFrozen(e.getPlayer())) {
                FreezeManager.get().remove(e.getPlayer());
            }
        }
    }

    private void sendMessage(String connection, Player p) {
        if (!FreezeManager.get().isFrozen(p.getUniqueId())) {
            return;
        }
        if (FreezeManager.get().isFreezeAll()) {
            return;
        }
        String msg = "";
        if (connection.equals("j")) {
            // join
            if (FreezeManager.get().getType(p) == FreezeType.PLAYER) {
                BarrierManager.get().add(p);
            }
            msg = Lang.NOTIFY_JOIN.toString().replace("%name%", p.getName());
        } else {
            // leave
            msg = Lang.NOTIFY_LEAVE.toString().replace("%name%", p.getName());
        }
        if (msg.equals("")) {
            return;
        }
        for (Player t : BukkitUtils.getOnlinePlayers()) {
            if (t.hasPermission(Lang.PERM_FREEZE_NOTIFY.toString()) || t.isOp()) {
                t.sendMessage(msg);
            }
        }
    }

}
