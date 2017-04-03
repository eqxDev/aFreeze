package me.eqxdev.afreeze.utils.chatroom;

import me.eqxdev.afreeze.utils.Lang;

import java.util.*;

/**
 * Created by eqxDev on 03/02/2017.
 */
public class ChatManager {

    private static ChatManager instance;

    public static ChatManager get() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public static Map<UUID, ChatRoom> chatrooms = new HashMap<>();
    // Player / ID
    public static Map<UUID, UUID> players = new HashMap<>();

    public List<UUID> getPlayerChatRoom(UUID chatroom) {
        List<UUID> playerList = new ArrayList<>();
        for (UUID uuid : players.keySet()) {
            if (players.get(uuid).equals(chatroom)) {
                playerList.add(uuid);
            }
        }
        return playerList;
    }

    public boolean inChatRoom(UUID user) {
        return players.containsKey(user) && chatrooms.containsKey(players.get(user));
    }

    public boolean chatroomHasPlayer(UUID user, UUID chatroom) {
        return getPlayerChatRoom(chatroom).contains(user);
    }

    public ChatRoom getChatRoom(UUID user) {
        return chatrooms.get(players.get(user));
    }

    public void add(UUID owner, UUID frozen) {
        if (chatrooms.containsKey(owner)) {
            getChatRoom(owner).sendMessage(Lang.CHATROOM_CONSOLE_USERNAME.toString(), Lang.CHATROOM_CONSOLE_JOIN.toString());
            players.put(frozen, owner);
        } else {
            chatrooms.put(owner, new ChatRoom(owner));
            players.put(owner, owner);
            players.put(frozen, owner);
            getChatRoom(owner).sendMessage(Lang.CHATROOM_CONSOLE_USERNAME.toString(), Lang.CHATROOM_CONSOLE_NEW.toString());
        }
    }

    public void remove(UUID owner, UUID frozen) {
        if (chatrooms.containsKey(owner)) {
            getChatRoom(owner).sendMessage(Lang.CHATROOM_CONSOLE_USERNAME.toString(), frozen.toString() + " has been removed from the chatroom.");
            players.remove(frozen, owner);
        }
    }

    public void delete(UUID owner) {
        getChatRoom(owner).sendMessage(Lang.CHATROOM_CONSOLE_USERNAME.toString(), Lang.CHATROOM_CONSOLE_DELETE.toString());
        for (UUID uuid : getPlayerChatRoom(owner)) {
            players.remove(uuid);
        }
        if (chatrooms.containsKey(owner)) {
            chatrooms.remove(owner);
        }
    }
}
