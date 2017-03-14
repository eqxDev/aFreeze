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
import me.eqxdev.afreeze.utils.factions.factions.*;
import me.eqxdev.afreeze.utils.redglass.BarrierHandler;
import me.esshd.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.parser.*;
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
        update();
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
        } else if(plugin.equalsIgnoreCase("FactionsUUID")) {
            faction = new FactionsUUID();
            getLogger().info("Your server is running: FactionsUUID.");
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
                try {
                    Object customhcf = com.customhcf.hcf.HCF.getPlugin();
                    faction = new IHCF_customhcf();
                    customhcf = null;
                    ConfigManager.get("config.yml").set("factions_type", "iHCF_customhcf");
                    ConfigManager.save(this,"config.yml");
                    getLogger().info("Your server is running: iHCF (customhcf).");
                } catch (NoClassDefFoundError e) {}
            }

            if(faction == null) {
                getServer().getLogger().severe("Can not find a supported version of iHCF, please contact me on spigot: eqx.");
            }
        } else if(plugin.equalsIgnoreCase("iHCF_esshd")) {
            faction = new IHCF_esshd();
            getLogger().info("Your server is running: iHCF (esshd).");
        } else if(plugin.equalsIgnoreCase("iHCF_customhcf")) {
            faction = new IHCF_customhcf();
            getLogger().info("Your server is running: iHCF (customhcf).");
        }
        return faction!=null;
    }
    private static final String USER_AGENT  = "MyUserAgent";// Change this!
    private static final String REQUEST_URL = "https://api.spiget.org/v2/resources/11582/versions";
    public static boolean NEW_UPDATE = false;
    public static String NEW_UPDATE_VER = "";
    private void update() {
        try {
            URL url = new URL(REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent" , USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            JSONArray versions = (JSONArray) JSONValue.parseWithException(reader);
            JSONObject version = (JSONObject) JSONValue.parseWithException(versions.get(versions.size()-1).toString());
            if(version.get("name").equals(getDescription().getVersion())) {
                getServer().getLogger().log(Level.INFO,"You are currently running the newest version.");
                NEW_UPDATE = false;
                NEW_UPDATE_VER = "";
            } else {
                getServer().getLogger().log(Level.WARNING,"There is a new version available " + version.get("name").toString() + ".");
                getServer().getLogger().log(Level.WARNING,"https://www.spigotmc.org/resources/afreeze.11582/updates");
                NEW_UPDATE_VER = version.get("name").toString();
                NEW_UPDATE = true;
            }

        } catch(IOException | ParseException e) {
            getServer().getLogger().log(Level.SEVERE,"Cannot connect to check for updates.");
        }
    }
}
