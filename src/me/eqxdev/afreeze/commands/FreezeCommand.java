package me.eqxdev.afreeze.commands;

import me.eqxdev.afreeze.Main;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.FreezeType;
import me.eqxdev.afreeze.utils.Lang;
import me.eqxdev.afreeze.utils.command.Command;
import me.eqxdev.afreeze.utils.redglass.BarrierManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by eqxDev on 03/02/2017.
 */
public class FreezeCommand {

    @Command(name="freeze",playerOnly = true,description = "Freeze a player",aliases = {"ss","afreeze"})
    public void command(CommandSender sender, String label, String[] args) {
        if(sender.isOp() && sender.hasPermission(Lang.PERM_FREEZE.toString())) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("-h")) { // hacker mode
                    if(!sender.hasPermission(Lang.PERM_FREEZE_HACKER.toString())) {
                        Lang.NO_PERMISSION.send(sender);
                        return;
                    }
                    if(!(args.length > 1)) {
                       Lang.FREEZE_HELP.send(sender);
                       return;
                    }
                    Player t = Bukkit.getPlayer(args[1]);
                    if (t == null && t.getName().equals(sender.getName())) {
                        // offline
                        Lang.PLAYER_NOT_FOUND.send(sender);
                        return;
                    }
                    if(t.hasPermission(Lang.PERM_FREEZE_BYPASS.toString())) {
                        Lang.FREEZE_CANNOT.send(sender);
                        return;
                    }
                    t.openInventory(Main.get().generateInventory());
                    toggle(t, sender, FreezeType.HACKER);
                } else if (args[0].equalsIgnoreCase("-g")) { // No glass
                    if(!sender.hasPermission(Lang.PERM_FREEZE_NO_GLASS.toString())) {
                        Lang.NO_PERMISSION.send(sender);
                        return;
                    }
                    if(!(args.length > 1)) {
                        Lang.FREEZE_HELP.send(sender);
                        return;
                    }
                    Player t = Bukkit.getPlayer(args[1]);
                    if (t == null && t.getName().equals(sender.getName())) {
                        // offline
                        Lang.PLAYER_NOT_FOUND.send(sender);
                        return;
                    }
                    if(t.hasPermission(Lang.PERM_FREEZE_BYPASS.toString())) {
                        Lang.FREEZE_CANNOT.send(sender);
                        return;
                    }
                    toggle(t, sender, FreezeType.NO_GLASS);
                } else if (args[0].equalsIgnoreCase("-f")) { // faction
                    if(!sender.hasPermission(Lang.PERM_FREEZE_FACTION.toString())) {
                        Lang.NO_PERMISSION.send(sender);
                        return;
                    }
                    if(!(args.length > 1)) {
                        Lang.FREEZE_HELP.send(sender);
                        return;
                    }
                    if(!Main.get().factionHook) {
                        Lang.ERROR_FACTION_HOOK.send(sender);
                        return;
                    }
                    Player t = Bukkit.getPlayer(args[1]);
                    if (t == null && t.getName().equals(sender.getName())) {
                        // offline
                        Lang.PLAYER_NOT_FOUND.send(sender);
                        return;
                    }
                    if(t.hasPermission(Lang.PERM_FREEZE_BYPASS.toString())) {
                        Lang.FREEZE_CANNOT.send(sender);
                        return;
                    }
                    for(Player player : Main.get().getFaction().getAllPlayerFor(t)) {
                        toggle(player, sender, FreezeType.FACTION);
                    }
                } else if (args[0].equalsIgnoreCase("-all")) { // all
                    if(!sender.hasPermission(Lang.PERM_FREEZE_ALL.toString())) {
                        Lang.NO_PERMISSION.send(sender);
                        return;
                    }
                    // all
                    if (FreezeManager.get().isFreezeAll()) {
                        // Broadcast server wide and to sender
                        Lang.UNFREEZE_ALL.send(sender);
                        Bukkit.broadcastMessage(Lang.UNFREEZE_ALL_BROADCAST.toString());
                        FreezeManager.get().setFreezeAll(false);
                        for (Player t : Bukkit.getOnlinePlayers()) {
                            if (FreezeManager.get().isFrozen(t)) {
                                FreezeManager.get().remove(t);
                            }
                        }

                    } else {
                        // Broadcast server wide and to sender
                        Lang.FREEZE_ALL.send(sender);
                        Bukkit.broadcastMessage(Lang.FREEZE_ALL_BROADCAST.toString());
                        FreezeManager.get().setFreezeAll(true);
                        for (Player t : Bukkit.getOnlinePlayers()) {
                            if(!t.hasPermission(Lang.PERM_FREEZE_SERVER_BYPASS.toString())) {
                                FreezeManager.get().add(t, FreezeType.NO_GLASS);
                            }
                        }
                    }

                } else { // normal
                    if(!sender.hasPermission(Lang.PERM_FREEZE.toString())) {
                        Lang.NO_PERMISSION.send(sender);
                        return;
                    }
                    Player t = Bukkit.getPlayer(args[0]);
                    if (t == null && t.getName().equals(sender.getName())) {
                        // offline
                        Lang.PLAYER_NOT_FOUND.send(sender);
                        return;
                    }
                    if(t.hasPermission(Lang.PERM_FREEZE_BYPASS.toString())) {
                        Lang.FREEZE_CANNOT.send(sender);
                        return;
                    }
                    toggle(t, sender, FreezeType.PLAYER);
                }
            } else {
                Lang.FREEZE_HELP.send(sender);
            }
        } else {
            Lang.NO_PERMISSION.send(sender);
        }
    }

    private void toggle(Player t, CommandSender sender, FreezeType freezeType) {
        if(FreezeManager.get().isFrozen(t)) {
            if(FreezeManager.get().getType(t) == FreezeType.PLAYER) {
                BarrierManager.get().get(t.getUniqueId()).update(true);
                BarrierManager.get().remove(t.getUniqueId());
            }
            if(FreezeManager.get().getType(t) == FreezeType.HACKER) {
                t.closeInventory();
            }
            FreezeManager.get().remove(t);
            Lang.UNFROZEN.send(t);
            sender.sendMessage(Lang.UNFROZEN_SUCCESS.toString().replace("%p%",t.getName()));
        } else {
            FreezeManager.get().add(t, freezeType, sender);
            sender.sendMessage(Lang.FREEZE_SUCCESS.toString().replace("%p%", t.getName()).replace("%mode%",freezeType.toName()));
            if(FreezeManager.get().getType(t) == FreezeType.PLAYER) {
                BarrierManager.get().add(t);
            }
        }
    }

}
