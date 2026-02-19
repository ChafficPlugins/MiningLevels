package de.chafficplugins.mininglevels;

import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.io.FileManager;
import de.chafficplugins.mininglevels.io.MessagesYaml;
import de.chafficplugins.mininglevels.listeners.MiningLevelsCommandListener;
import de.chafficplugins.mininglevels.listeners.RewardCommandListener;
import de.chafficplugins.mininglevels.listeners.events.MiningEvents;
import de.chafficplugins.mininglevels.listeners.events.NoXpBlockEvents;
import de.chafficplugins.mininglevels.listeners.events.ServerEvents;
import de.chafficplugins.mininglevels.placeholders.LevelPlaceholders;
import de.chafficplugins.mininglevels.utils.Crucial;
import de.chafficplugins.mininglevels.utils.CustomMessages;
import io.github.chafficui.CrucialLib.Utils.Server;
import io.github.chafficui.CrucialLib.Utils.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class MiningLevels extends JavaPlugin {
    private final Logger logger = Logger.getLogger("MiningLevels");
    public FileManager fileManager;
    public CustomMessages customMessages;
    public boolean placeholderAPI = false;

    @Override
    public void onLoad() {
        Crucial.init();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            if (!Server.checkCompatibility("1.21")) {
                warn("Unsupported server version, there may be some issues with this version. Please use a supported version.");
                warn("This is NOT a bug. Do NOT report this!");
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
                    placeholderAPI = true;
                } else {
                    warn("PlaceholderAPI not found. Placeholders and Command Rewards will not work. Consider installing PlaceholderAPI.");
                }

                //Init localizations
                MessagesYaml.create();
                customMessages = new CustomMessages();

                registerCommand("mininglevels", new MiningLevelsCommandListener());
                registerCommand("miningrewards", new RewardCommandListener());
                registerEvents(new MiningEvents(), new ServerEvents(), new NoXpBlockEvents());
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
        getConfig().addDefault(LEVEL_WITH_GENERATED_BLOCKS, false);
        getConfig().addDefault(LEVEL_PROGRESSION_MESSAGES, "actionBar");
        getConfig().addDefault(DESTROY_MINING_BLOCKS_ON_EXPLODE, true);
        getConfig().addDefault(ADMIN_DEBUG, false);
        getConfig().addDefault(MINING_ITEMS, new String[]{
                Material.DIAMOND_PICKAXE.name(),
                Material.GOLDEN_PICKAXE.name(),
                Material.IRON_PICKAXE.name(),
                Material.STONE_PICKAXE.name(),
                Material.WOODEN_PICKAXE.name(),
                Material.NETHERITE_PICKAXE.name()
        });
        getConfig().addDefault("prefix", "§8[§6ML§8] §r");
        getConfig().addDefault("language", "CUSTOM");
        getConfig().options().copyDefaults(true);
        saveConfig();

        try {
            lvlUpSound = Sound.valueOf(getConfigString(LVL_UP_SOUND).toUpperCase(Locale.ROOT));
        } catch (NullPointerException | IllegalArgumentException e) {
            error("Config value levelup sound is either misspelled or missing! Using ENTITY_PLAYER_LEVELUP");
        }
        PREFIX = getConfigString("prefix");
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

    public void warn(String message) {
        logger.warning(message);
    }
}
