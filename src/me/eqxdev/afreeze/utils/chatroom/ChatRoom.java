package me.eqxdev.afreeze.utils.chatroom;

import me.eqxdev.afreeze.utils.Lang;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eqxDev on 03/02/2017.
 */
public class ChatRoom {

    private UUID owner;

    public ChatRoom(UUID owner) {
        this.owner = owner;
    }

    private String format = null;

    public void sendMessage(String name, String msg) {
        for (UUID uuid: ChatManager.get().getPlayerChatRoom(owner)) {
            Player p = Bukkit.getPlayer(uuid);
            if(p !=null) {
                if(format == null) {
                    format = Lang.CHATROOM_FORMAT.toString();
                }
                p.sendMessage(format.replace("%name%", name).replace("%msg%", msg));
            }
        }
    }

    public List<UUID> getPlayers() {
        List<UUID> uuids = new ArrayList<>();
        for(UUID uuid : ChatManager.get().getPlayerChatRoom(owner)) {
            uuids.add(uuid);
        }
        return uuids;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
