package me.eqxdev.afreeze.utils.command;

import me.eqxdev.afreeze.utils.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.*;

public class CommandRegistry {


    private static final SimpleCommandMap commandMap;
    private static final HelpMap helpMap;

    static {
        helpMap = Bukkit.getServer().getHelpMap();

        Reflection.SafeClass sc = Reflection.getSafeClass("{obc}.{v}.CraftServer");
        sc.setObject(Bukkit.getServer());

        commandMap = (SimpleCommandMap)sc.getField("commandMap").get();
    }

    private final Plugin plugin;
    private List<org.bukkit.command.Command> registered = new ArrayList<>();

    public CommandRegistry(Plugin plugin){
        this.plugin = plugin;

        helpMap.addTopic(new IndexHelpTopic(plugin.getName(), "All commands for " + plugin.getName(), null, new LinkedList<>(), "Below is a list of all " + plugin.getName() + " commands:"));
    }

    public void register(org.bukkit.command.Command command){
        if (commandMap == null) {
            throw new RuntimeException("Could not find SimpleCommandMap");
        }
        if(helpMap == null){
            throw new RuntimeException("Could not find HelpMap");
        }

        commandMap.register(plugin.getName(), command);

        IndexHelpTopic topic = (IndexHelpTopic)helpMap.getHelpTopic(plugin.getName());

        GenericCommandHelpTopic topic1 = new GenericCommandHelpTopic(command);

        Reflection.SafeClass sc = Reflection.getSafeClass(topic);
        sc.setObject(topic);

        Reflection.SafeField sf = sc.getField("allTopics");
        ((Collection<HelpTopic>)sf.get()).add(topic1);

        registered.add(command);
    }

    public void register(Object object, TabCompleter completer){
        Class<?> cls = object.getClass();

        for(Method method : cls.getDeclaredMethods()){
            Class<?>[] types = method.getParameterTypes();

            if(types[0] == CommandSender.class && types[1] == String.class && types[2] == String[].class) {
                if (method.isAnnotationPresent(Command.class)) {
                    Command ann = method.getAnnotation(Command.class);

                    this.register(new org.bukkit.command.Command(ann.name(), ann.description(), "/" + ann.name(), Arrays.asList(ann.aliases())) {
                        @Override
                        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
                            return completer.onTabComplete(sender, this, alias, args);
                        }

                        @Override
                        public boolean execute(CommandSender sender, String label, String[] args) {
                            if(ann.playerOnly()){
                                if(!(sender instanceof Player)){
                                    sender.sendMessage(ChatColor.RED + "Only a player can use that command.");
                                    return true;
                                }
                            }

                            if(!ann.permission().equals("")){
                                if(!sender.hasPermission(ann.permission())){
                                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                                    return true;
                                }
                            }

                            try {
                                method.invoke(object, sender, label, args);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Internal error while attempting to execute /" + label);
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while executing the command /" + label);
                                e.printStackTrace();
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }

    public void register(Object object){

        Class<?> cls = object.getClass();

        for(Method method : cls.getDeclaredMethods()){
            Class<?>[] types = method.getParameterTypes();

            if(types[0] == CommandSender.class && types[1] == String.class && types[2] == String[].class) {
                if (method.isAnnotationPresent(Command.class)) {
                    Command ann = method.getAnnotation(Command.class);

                    this.register(new org.bukkit.command.Command(ann.name(), ann.description(), "/" + ann.name(), Arrays.asList(ann.aliases())) {


                        @Override
                        public boolean execute(CommandSender sender, String label, String[] args) {
                            if(ann.playerOnly()){
                                if(!(sender instanceof Player)){
                                    sender.sendMessage(ChatColor.RED + "Only a player can use that command.");
                                    return true;
                                }
                            }

                            if(!ann.permission().equals("")){
                                if(!sender.hasPermission(ann.permission())){
                                    sender.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
                                    return true;
                                }
                            }

                            try {
                                method.invoke(object, sender, label, args);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "Internal error while attempting to execute /" + label);
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while executing the command /" + label);
                                e.printStackTrace();
                            }
                            return true;
                        }
                    });
                }
            }
        }

    }

    public void unregister(org.bukkit.command.Command command){
        unregister(command, true);
    }

    public List<org.bukkit.command.Command> getRegisteredCommands(){
        return this.registered;
    }

    public void unregisterAll(){
        Iterator<org.bukkit.command.Command> it = registered.iterator();
        while(it.hasNext()){
            unregister(it.next(), false);
            it.remove();
        }
    }

    private void unregister(org.bukkit.command.Command command, boolean remove){
        if (commandMap == null) {
            throw new RuntimeException("Could not find SimpleCommandMap");
        }
        if(helpMap == null){
            throw new RuntimeException("Could not find HelpMap");
        }

        Map m = (Map)Reflection.getSafeClass(commandMap).getField("knownCommands").get();
        if(m.containsKey(command.getName())) m.remove(command.getName());

        IndexHelpTopic topic = (IndexHelpTopic)helpMap.getHelpTopic(plugin.getName());

        Reflection.SafeClass sc = Reflection.getSafeClass(topic);
        sc.setObject(topic);

        Collection collection = (Collection)sc.getField("allTopics").get();

        Iterator<Object> it = collection.iterator();

        while(it.hasNext()){
            Object o = it.next();
            if(o instanceof GenericCommandHelpTopic){
                GenericCommandHelpTopic c = (GenericCommandHelpTopic)o;
                if(c.getName().equals("/" + command.getName()) || c.getName().equals(command.getName()) || c.getName().equals(plugin.getName() + ":" + command.getName())){
                    it.remove();
                }
            }
        }

        if(remove) registered.remove(command);
    }

    public static org.bukkit.command.Command toCommand(CommandExecutor executor, String name){
        return new org.bukkit.command.Command(name) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                executor.onCommand(sender, this, commandLabel, args);
                return true;
            }
        };
    }

    public static org.bukkit.command.Command toCommand(CommandExecutor executor, String name, String description, String usage, String... aliases){
        return new org.bukkit.command.Command(name) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                executor.onCommand(sender, this, commandLabel, args);
                return true;
            }
        };
    }


}
