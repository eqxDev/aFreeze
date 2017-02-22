package me.eqxdev.afreeze;

import me.eqxdev.afreeze.commands.FreezeCommand;
import me.eqxdev.afreeze.listeners.ChatHandler;
import me.eqxdev.afreeze.listeners.ConnectionEvents;
import me.eqxdev.afreeze.listeners.FreezeHandler;
import me.eqxdev.afreeze.runnable.FreezeRunnable;
import me.eqxdev.afreeze.utils.ConfigManager;
import me.eqxdev.afreeze.utils.FreezeManager;
import me.eqxdev.afreeze.utils.Lang;
import me.eqxdev.afreeze.utils.chatroom.ChatManager;
import me.eqxdev.afreeze.utils.command.CommandRegistry;
import me.eqxdev.afreeze.utils.factions.Faction;
import me.eqxdev.afreeze.utils.factions.factions.Factions;
import me.eqxdev.afreeze.utils.factions.factions.HCFactions;
import me.eqxdev.afreeze.utils.factions.factions.IHCF_esshd;
import me.eqxdev.afreeze.utils.factions.factions.Mango;
import me.eqxdev.afreeze.utils.redglass.BarrierHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Created by eqxDev.
 */

/*

    TODO:
        - Bug test
            - Freeze multible people then unfreeze one.
            -
 */
public class Main extends JavaPlugin {

    private static Main pl;
    public static Main get() {return pl;}

    private YamlConfiguration LANG;
    private File LANG_FILE;
    private CommandRegistry commandRegistry;

    public boolean factionHook = true;
    private Faction faction = null;
    public Faction getFaction() {
        return faction;
    }

    @Override
    public void onEnable() {
        pl = this;
        lang();
        ConfigManager.load(this, "config.yml");
        this.commandRegistry = new CommandRegistry(this);

        ConfigManager.load(this,"config.yml");

        if(!setupFaction()) {
            getServer().getLogger().severe("Could not setup Faction hook.");
            factionHook = false;
        }

        ChatManager.get();
        FreezeManager.get();
        registerCommands();
        registerListeners();

        BukkitTask bt = new FreezeRunnable().runTaskTimer(this,10L,10L);
        generateInventory();
    }

    @Override
    public void onDisable() {
        pl = null;
        System.gc();
    }


    public CommandRegistry getCommandRegistry(){
        return this.commandRegistry;
    }
    private void registerCommands() {
        getCommandRegistry().register(new FreezeCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new FreezeHandler(), this);
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new BarrierHandler(), this);
        getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);
    }

    // Language file

    public YamlConfiguration getLang()
    {
        return this.LANG;
    }

    public File getLangFile()
    {
        return this.LANG_FILE;
    }

    private void lang() {

        File lang = new File(getDataFolder(), "messages.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = this.getResource("messages.yml");
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                    FileWriter fw = new FileWriter(lang);
                    fw.write("# messages.yml");
                    defConfig.save(lang);
                    Lang.setFile(defConfig);
                }
            } catch (IOException e) {
                e.printStackTrace(); // So they notice
                getServer().getLogger().severe("[aFreeze] Couldn't create language file.");
                getServer().getLogger().severe("[aFreeze] This is a fatal error. Now disabling");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for(Lang item:Lang.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }
        Lang.setFile(conf);
        LANG = conf;
        LANG_FILE = lang;
        try {
            conf.save(getLangFile());
        } catch (IOException e) {
            getServer().getLogger().log(Level.WARNING, "[aFreeze]: Failed to save lang.yml.");
            getServer().getLogger().log(Level.WARNING, "[aFreeze]: Report this stack trace to eqx.");
        }

    }

    private Inventory inventory = null;
    public String titleInv = null;
    public Inventory generateInventory() {
        if(titleInv == null) {
            titleInv = Lang.FROZEN_INV_TITLE.toString().substring(0,31);
        }
        if(inventory == null) {
            inventory = Bukkit.createInventory(null, 9, titleInv);
            ItemStack isis = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)14);
            ItemMeta imim = isis.getItemMeta();
            imim.setDisplayName(Lang.FROZEN_ITEM_TITLE.toString());
            isis.setItemMeta(imim);
            for(int i=0; i < 9; i++) {
                inventory.setItem(i,isis);
            }
        }
        return inventory;
    }

    private boolean setupFaction() {
        String plugin = ConfigManager.get("config.yml").getString("factions_type");
        if(plugin.equalsIgnoreCase("HCFactions")) {
            faction = new HCFactions();
            getLogger().info("Your server is running: HCFactions.");
        } else if(plugin.equalsIgnoreCase("Factions")) {
            faction = new Factions();
            getLogger().info("Your server is running: Factions.");
        } else if(plugin.equalsIgnoreCase("Mango")) {
            faction = new Mango();
            getLogger().info("Your server is running: Mango.");
        } else if(plugin.equalsIgnoreCase("iHCF")) {
            try {
                Object esshd = me.esshd.hcf.HCF.getPlugin();
                faction = new IHCF_esshd();
                esshd = null;
                ConfigManager.get("config.yml").set("factions_type", "iHCF_esshd");
                ConfigManager.save(this,"config.yml");
                getLogger().info("Your server is running: iHCF (esshd).");
            } catch (NoClassDefFoundError e) {}

            if(faction == null) {
                getServer().getLogger().severe("Can not find a supported version of iHCF, please contact me on spigot: eqx.");
            }
        } else if(plugin.equalsIgnoreCase("iHCF_esshd")) {
            faction = new IHCF_esshd();
            getLogger().info("Your server is running: iHCF (esshd).");
        }
        return faction!=null;
    }

}
