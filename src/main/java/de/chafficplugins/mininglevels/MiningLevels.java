package de.chafficplugins.mininglevels;

import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.io.FileManager;
import de.chafficplugins.mininglevels.io.MessagesYaml;
import de.chafficplugins.mininglevels.listeners.MiningLevelsCommandListener;
import de.chafficplugins.mininglevels.listeners.RewardCommandListener;
import de.chafficplugins.mininglevels.listeners.events.MiningEvents;
import de.chafficplugins.mininglevels.listeners.events.ServerEvents;
import de.chafficplugins.mininglevels.placeholders.LevelPlaceholders;
import de.chafficplugins.mininglevels.utils.Crucial;
import de.chafficplugins.mininglevels.utils.CustomMessages;
import io.github.chafficui.CrucialAPI.Utils.Server;
import io.github.chafficui.CrucialAPI.Utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public final class MiningLevels extends JavaPlugin {
    private final Logger logger = Logger.getLogger("MiningLevels");
    public FileManager fileManager;

    @Override
    public void onLoad() {
        Crucial.download();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            if (!Server.checkCompatibility("1.18", "1.17", "1.16", "1.15")) {
                error("Wrong server version. Please use a supported version.");
                error("This is NOT a bug. Do NOT report this!");
                throw new IOException();
            }

            if(Crucial.connect()) {
                loadConfig();
                fileManager = new FileManager();
                MiningLevel.init();
                MiningPlayer.init();
                MiningBlock.init();

                //Register Placeholders
                if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    log("PlaceholderAPI found. Registering Placeholders.");
                    new LevelPlaceholders().register();
                }

                //Init localizations
                MessagesYaml.create();
                new CustomMessages();

                registerCommand("mininglevels", new MiningLevelsCommandListener());
                registerCommand("miningrewards", new RewardCommandListener());
                registerEvents(new MiningEvents(), new ServerEvents());
                new Stats(this, BSTATS_ID);
                log(ChatColor.DARK_GREEN + getDescription().getName() + " is now enabled (Version: " + getDescription().getVersion() + ") made by "
                        + ChatColor.AQUA + getDescription().getAuthors() + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("Failed to startup " + getDescription().getName() + " (Version: " + getDescription().getVersion() + ")");
            getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
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

    public static Sound lvlUpSound;

    private void loadConfig() {
        //getConfig().addDefault(AUTO_UPDATE, true);
        getConfig().addDefault(LVL_UP_SOUND, Sound.ENTITY_PLAYER_LEVELUP.name());
        getConfig().addDefault(MAX_LEVEL_XP_DROPS, false);
        getConfig().addDefault(LEVEL_WITH_PLAYER_PLACED_BLOCKS, false);
        getConfig().addDefault("language", "CUSTOM");
        getConfig().options().copyDefaults(true);
        saveConfig();

        try {
            lvlUpSound = Sound.valueOf(getConfigString(LVL_UP_SOUND).toUpperCase(Locale.ROOT));
        } catch (NullPointerException | IllegalArgumentException e) {
            error("Config value levelup sound is either misspelled or missing! Using ENTITY_PLAYER_LEVELUP");
        }
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
