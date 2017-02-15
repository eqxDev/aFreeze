package me.eqxdev.afreeze.listeners;

import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.Lang;
import org.bukkit.Bukkit;
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
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        sendMessage("l",e.getPlayer());
    }

    private void sendMessage(String connection, Player p) {
        if(!FreezeManager.get().isFrozen(p.getUniqueId())) {
            return;
        }
        String msg = "";
        if(connection.equals("j")) {
            // join
            msg = Lang.NOTIFY_JOIN.toString().replace("%name%",p.getName());
        } else {
            // leave
            msg = Lang.NOTIFY_LEAVE.toString().replace("%name%",p.getName());
        }
        for(Player t : Bukkit.getOnlinePlayers()) {
            if(t.hasPermission(Lang.PERM_FREEZE_NOTIFY.toString()) || t.isOp()) {
                t.sendMessage(msg);
            }
        }
    }

}
