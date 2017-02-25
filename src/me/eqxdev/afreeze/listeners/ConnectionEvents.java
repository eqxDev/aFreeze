package me.eqxdev.afreeze.listeners;

import me.eqxdev.afreeze.utils.BukkitUtils;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.FreezeType;
import me.eqxdev.afreeze.utils.Lang;
import me.eqxdev.afreeze.utils.redglass.BarrierManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by eqxDev on 13/02/2017.
 */
public class ConnectionEvents implements Listener {

    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        sendMessage("j", e.getPlayer());
        if(FreezeManager.get().isFreezeAll()) {
            if(!FreezeManager.get().isFrozen(e.getPlayer())) {
                FreezeManager.get().add(e.getPlayer(),FreezeType.ALL);
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        sendMessage("l",e.getPlayer());
        if(FreezeManager.get().isFreezeAll()) {
            if(FreezeManager.get().isFrozen(e.getPlayer())) {
                FreezeManager.get().remove(e.getPlayer());
            }
        }
    }

    private void sendMessage(String connection, Player p) {
        if(!FreezeManager.get().isFrozen(p.getUniqueId())) {
            return;
        }
        if(FreezeManager.get().isFreezeAll()) {
            return;
        }
        String msg = "";
        if(connection.equals("j")) {
            // join
            if(FreezeManager.get().getType(p) == FreezeType.PLAYER) {
                BarrierManager.get().add(p);
            }
            msg = Lang.NOTIFY_JOIN.toString().replace("%name%",p.getName());
        } else {
            // leave
            msg = Lang.NOTIFY_LEAVE.toString().replace("%name%",p.getName());
        }
        if(msg.equals("")) {
            return;
        }
        for(Player t : BukkitUtils.getOnlinePlayers()) {
            if(t.hasPermission(Lang.PERM_FREEZE_NOTIFY.toString()) || t.isOp()) {
                t.sendMessage(msg);
            }
        }
    }

}
