package me.eqxdev.afreeze.listeners;

import me.eqxdev.afreeze.utils.BukkitUtils;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.Lang;
import me.eqxdev.afreeze.utils.chatroom.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by eqxDev on 03/02/2017.
 */
public class ChatHandler implements Listener {

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        if(FreezeManager.get().isFreezeAll() && !e.getPlayer().hasPermission(Lang.PERM_FREEZE_SERVER_BYPASS.toString())) {
            e.setCancelled(true);
            for(Player p : BukkitUtils.getOnlinePlayers()) {
                p.getName();
            }
            return;
        }

        if(ChatManager.chatrooms.size() > 0) {
            if(FreezeManager.get().isFrozen(e.getPlayer()) || ChatManager.chatrooms.containsKey(e.getPlayer().getUniqueId())) {
                if(!isOnline(ChatManager.get().getChatRoom(e.getPlayer().getUniqueId()).getPlayers())) {
                    return;
                }
                ChatManager.get().getChatRoom(e.getPlayer().getUniqueId()).sendMessage(e.getPlayer().getName(),e.getMessage());
                e.setCancelled(true);
            } else {
                for(UUID players : ChatManager.players.keySet()) {
                    e.getRecipients().remove(Bukkit.getPlayer(players));
                }
            }
        }
    }

    private boolean isOnline(List<UUID> uuids) {
        int online = 0;
        for (UUID uuid : uuids) {
            Player t = Bukkit.getPlayer(uuid);
            if(t!=null) {
                online++;
            }
        }
        return online > 1;
    }

}
