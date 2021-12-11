package de.chaffic.advancedrpgmining;

import de.chaffic.advancedrpgmining.api.MiningBlock;
import de.chaffic.advancedrpgmining.api.MiningLevel;
import de.chaffic.advancedrpgmining.api.MiningPlayer;
import de.chaffic.advancedrpgmining.io.FileManager;
import de.chaffic.advancedrpgmining.listeners.CommandListener;
import de.chaffic.advancedrpgmining.listeners.events.MiningEvents;
import de.chaffic.advancedrpgmining.listeners.events.ServerEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class AdvancedRPGMining extends JavaPlugin {
    private final Logger logger = Logger.getLogger("ARMining");
    public FileManager fileManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            fileManager = new FileManager();
            MiningLevel.init();
            MiningPlayer.init();
            MiningBlock.init();
            registerCommand("advancedrpgmining", new CommandListener());
            registerEvents(new MiningEvents(), new ServerEvents());
        } catch (IOException e) {
            getPluginLoader().disablePlugin(this);
            return;
        }

        log(ChatColor.DARK_GREEN + getDescription().getName() + " is now enabled (Version: " + getDescription().getVersion() + ") made by "
                + ChatColor.AQUA + getDescription().getAuthors() + ".");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            MiningLevel.save();
            MiningBlock.save();
            MiningPlayer.save();
        } catch (IOException e) {
            error("Failed to save files.");
        }
        Bukkit.getScheduler().cancelTasks(this);
        log(ChatColor.DARK_GREEN + getDescription().getName() + " is now disabled (Version: " + getDescription().getVersion() + ")");
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor(executor);
        }
    }

    private void loadConfig() {
        //getConfig().addDefault(AUTO_UPDATE, true);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public boolean getConfigBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public int getConfigInt(String path) {
        return getConfig().getInt(path);
    }

    public String getConfigString(String path) {
        return getConfig().getString(path);
    }

    public void log(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.severe(message);
    }
}
