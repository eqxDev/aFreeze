package me.eqxdev.afreeze.utils;

import me.eqxdev.afreeze.utils.chatroom.ChatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by eqxDev.
 */
public class FreezeManager {

    private static FreezeManager instance;
    public static FreezeManager get() {
        if(instance == null) {instance = new FreezeManager(); }
        return instance;
    }

    private Map<UUID, FreezeType> freeze = new HashMap<>();

    public Map<UUID, FreezeType> getFreeze() {
        return freeze;
    }

    public void add(Player p) {
        freeze.put(p.getUniqueId(), FreezeType.PLAYER);
        message(p);
    }
    public void add(Player p, FreezeType type, CommandSender sender) {
        freeze.put(p.getUniqueId(), type);
        message(p);
        ChatManager.get().add(((Player)sender).getUniqueId(),p.getUniqueId());
    }

    public void add(Player p, FreezeType type) {
        freeze.put(p.getUniqueId(), type);
    }

    public void remove(Player p) {
        freeze.remove(p.getUniqueId());
        if(ChatManager.get().inChatRoom(p.getUniqueId())) {
            if(ChatManager.get().getPlayerChatRoom(ChatManager.get().getChatRoom(p.getUniqueId()).getOwner()).size() > 1) { // 0 is admin 1 is someone 1 > is more than one person
                int frozen = 0;
                for(UUID uuid :ChatManager.get().getPlayerChatRoom(ChatManager.get().getChatRoom(p.getUniqueId()).getOwner())) {
                    Player t = Bukkit.getPlayer(uuid);
                    if (t == null) {
                        continue;
                    }
                    if (FreezeManager.get().isFrozen(uuid)) {
                        frozen++;
                    }
                    if (frozen > 1) {
                        ChatManager.get().remove(ChatManager.get().getChatRoom(p.getUniqueId()).getOwner(), p.getUniqueId());
                    } else {
                        ChatManager.get().delete(ChatManager.get().getChatRoom(p.getUniqueId()).getOwner());
                    }
                }
            } else {
                ChatManager.get().delete(ChatManager.get().getChatRoom(p.getUniqueId()).getOwner());
            }
        }
    }
    public boolean isFrozen(Player p) {
        return freeze.containsKey(p.getUniqueId());
    }
    public boolean isFrozen(UUID p) {
        return freeze.containsKey(p);
    }
    public FreezeType getType(Player p) { return freeze.get(p.getUniqueId()); }

    public FreezeType getType(UUID p) { return freeze.get(p); }

    public void message(Player p) {
        Lang.FROZEN.send(p);
    }

    private boolean freezeAll = false;

    public boolean isFreezeAll() {
        return freezeAll;
    }

    public void setFreezeAll(boolean freezeAll) {
        this.freezeAll = freezeAll;
    }

    public boolean hasFrozenPlayers() {return freeze.size() > 0; }
}
