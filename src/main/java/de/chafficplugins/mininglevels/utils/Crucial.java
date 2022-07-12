package de.chafficplugins.mininglevels.utils;

import de.chafficplugins.mininglevels.MiningLevels;
import io.github.chafficui.CrucialAPI.Utils.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.CRUCIAL_API_VERSION;

public class Crucial {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    private static boolean isConnected = false;

    public static void init() {
        Plugin crucialAPI = plugin.getServer().getPluginManager().getPlugin("CrucialAPI");
        if (crucialAPI == null) {
            try {
                download();
            } catch (IOException e) {
                plugin.error("Error 24: Failed to download CrucialAPI");
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                Bukkit.getPluginManager().disablePlugin(plugin);
            } catch (InvalidDescriptionException | org.bukkit.plugin.InvalidPluginException e) {
                plugin.error("Error 25: Failed to load CrucialAPI.");
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        } else if (!checkVersion(crucialAPI)) {
            plugin.error("Error 26: Wrong version of CrucialAPI.");
            plugin.getServer().getPluginManager().disablePlugin(crucialAPI);
            try {
                download();
            } catch (IOException e) {
                plugin.error("Error 24: Failed to download CrucialAPI");
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                Bukkit.getPluginManager().disablePlugin(plugin);
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                plugin.error("Error 25: Failed to load CrucialAPI.");
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    private static void download() throws IOException, InvalidPluginException, InvalidDescriptionException {
        plugin.log("Downloading CrucialAPI");
        URL website = new URL("https://github.com/Chafficui/CrucialAPI/releases/download/v" + CRUCIAL_API_VERSION + "/CrucialAPI-v" + CRUCIAL_API_VERSION + ".jar");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("plugins/CrucialAPI.jar");
        fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
        plugin.log(ChatColor.GREEN + "Downloaded successfully.");
        Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialAPI.jar"));
    }

    private static boolean checkVersion(Plugin crucialAPI) {
        String version = crucialAPI.getDescription().getVersion();
        if(version.equals(CRUCIAL_API_VERSION)) return true;
        String[] subVersions = version.split("\\.");
        String[] subVersions2 = CRUCIAL_API_VERSION.split("\\.");
        if(subVersions[0].equals(subVersions2[0]) && subVersions[1].equals(subVersions2[1])) {
            return Integer.parseInt(subVersions[2]) >= Integer.parseInt(subVersions2[2]);
        }
        return false;
    }

    public static boolean connect() throws IOException {
        org.bukkit.plugin.Plugin crucialAPI = plugin.getServer().getPluginManager().getPlugin("CrucialAPI");
        if (crucialAPI != null) {
            if (!crucialAPI.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(crucialAPI);
            }
            plugin.log(ChatColor.GREEN + "Successfully connected to CrucialAPI.");
            isConnected = true;
            if (!crucialAPI.getDescription().getVersion().startsWith(CRUCIAL_API_VERSION.substring(0,3))) {
                plugin.error("Error 24: Please update to CrucialAPI " + CRUCIAL_API_VERSION);
                plugin.log("Please download it from: https://www.spigotmc.org/resources/crucialapi.86380/");
                return false;
            }
            return true;
        }
        plugin.error("Error 26: Failed to connect to CrucialAPI.");
        return false;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }
}