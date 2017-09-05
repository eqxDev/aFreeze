package me.eqxdev.afreeze.utils;

import me.eqxdev.afreeze.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by eqxDev on 2/25/2017.
 */
public class BukkitUtils {

    public static Collection<? extends Player> getOnlinePlayers() {
        try {
            Method onlinePlayerMethod = Server.class.getMethod("getOnlinePlayers");
            if (onlinePlayerMethod.getReturnType().equals(Collection.class)) {
                return ((Collection<? extends Player>) onlinePlayerMethod.invoke(Bukkit.getServer()));
            } else {
                return Arrays.asList((Player[]) onlinePlayerMethod.invoke(Bukkit.getServer()));
            }
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while getting online players.");
            ex.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(Main.get());
        }
        return new HashSet<>();
    }

}
