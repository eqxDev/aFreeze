package me.eqxdev.afreeze.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by eqxDev on 03/02/2017.
 */
public enum Lang {

    UNFROZEN_SUCCESS("UNFROZEN_SUCCESS", "&aYou have unfrozen %p%."),
    UNFROZEN("UNFROZEN", "&aYou have been unfrozen, you are free to rome the server!"),
    FROZEN("FROZEN", "&cYou have been frozen!"),
    FREEZE_SUCCESS("FREEZE_SUCCESS", "&aYou have frozen %p%! &7[%mode%]"),
    FREEZE_ALL_BROADCAST("FREEZE_ALL_BROADCAST", "&7[&caFreeze&7] &cThe whole server has been frozen."),
    FREEZE_CANNOT("FREEZE_CANNOT", "&cYou cannot freeze this player."),
    FREEZE_ALL("FREEZE_ALL", "You have frozen all the players on the server."),
    UNFREEZE_ALL_BROADCAST("UNFREEZE_ALL_BROADCAST", "&7[&caFreeze&7] &cThe whole server has been unfrozen."),
    UNFREEZE_ALL("UNFREEZE_ALL", "You have unfrozen all the players on the server."),
    PLAYER_NOT_FOUND("PLAYER_NOT_FOUND", "&cPlayer not found."),
    FROZEN_INV_TITLE("FROZEN_INV_TITLE", "&cYou have been frozen please join the teamspeak: ts.teamspeak.com"),
    FROZEN_ITEM_TITLE("FROZEN_ITEM_TITLE", "&cFROZEN!"),
    FROZEN_BUILD("FROZEN_BUILD", "&cYou cannot interact while frozen."),
    FREEZE_HELP("FREEZE_HELP", "/freeze /ss /afreeze (All work - Do for help message) \n /ss <player> (Normal) \n /ss -f <player> (Faction) \n /ss -g <player> (No Glass) - /ss -h <player> (Hacker) \n /ss -all (Everyone)"),
    CHATROOM_CONSOLE_USERNAME("CHATROOM_CONSOLE_USERNAME", "&fConsole"),
    CHATROOM_CONSOLE_JOIN("CHATROOM_CONSOLE_JOIN", "&cNew player has been added to the chat."),
    CHATROOM_CONSOLE_NEW("CHATROOM_CONSOLE_NEW", "&cNew chat has been created."),
    CHATROOM_CONSOLE_DELETE("CHATROOM_CONSOLE_DELETE", "&cChat deleted."),
    CHATROOM_FORMAT("CHATROOM_FORMAT", "%name%: %msg%"),
    NOTIFY_LEAVE("NOTIFY_LEAVE", "%name% has left the server while frozen."),
    NOTIFY_JOIN("NOTIFY_JOIN", "%name% has joined the server while frozen."),
    NO_PERMISSION("NO_PERMISSION", "&cNo Permission."),
    ERROR_FACTION_HOOK("ERROR_FACTION_HOOK","&cThe faction hook has been disabled, check console and your config file."),

    PERM_FREEZE_NOTIFY("PERM_FREEZE_NOTIFY", "afreeze.notify"),
    PERM_FREEZE_BYPASS("PERM_FREEZE_BYPASS", "afreeze.bypass"),
    PERM_FREEZE_SERVER_BYPASS("PERM_FREEZE_BYPASS", "afreeze.bypass"),
    PERM_FREEZE_HACKER("PERM_FREEZE_HACKER", "afreeze.hacker"),
    PERM_FREEZE_NO_GLASS("PERM_FREEZE_NO_GLASS", "afreeze.noglass"),
    PERM_FREEZE_FACTION("PERM_FREEZE_FACTION", "afreeze.faction"),
    PERM_FREEZE_ALL("PERM_FREEZE_ALL", "afreeze.all"),
    PERM_FREEZE("PERM_FREEZE", "afreeze.");

    private final String path;
    private final String def;
    private static FileConfiguration LANG;

    private Lang(String path, String start)
    {
        this.path = path;
        this.def = start;
    }

    public static void setFile(FileConfiguration config)
    {
        LANG = config;
    }

    public String toString()
    {
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, this.def));
    }

    public String[] toArray() {
        return toString().split("\n");
    }

    public void send(Player p)
    {
        for(String msg : toArray()) {
            p.sendMessage(msg);
        }
    }

    public void send(CommandSender p)
    {
        for(String msg : toArray()) {
            p.sendMessage(msg);
        }
    }

    public String getDefault()
    {
        return this.def;
    }

    public String getPath()
    {
        return this.path;
    }

}
