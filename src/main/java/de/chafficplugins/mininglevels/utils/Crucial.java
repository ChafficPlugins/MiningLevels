package de.chafficplugins.mininglevels.utils;

import de.chafficplugins.mininglevels.MiningLevels;
import io.github.chafficui.CrucialLib.Utils.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.CRUCIAL_LIB_VERSION;

public class Crucial {
    private static MiningLevels pluginInstance;
    private static MiningLevels plugin() {
        if (pluginInstance == null) pluginInstance = MiningLevels.getPlugin(MiningLevels.class);
        return pluginInstance;
    }
    private static boolean isConnected = false;

    public static void init() {
        Plugin crucialLib = plugin().getServer().getPluginManager().getPlugin("CrucialLib");
        if (crucialLib == null) {
            try {
                download();
            } catch (IOException e) {
                plugin().error("Error 24: Failed to download CrucialLib");
                plugin().log("Please download it from: https://github.com/ChafficPlugins/CrucialLib/releases");
                Bukkit.getPluginManager().disablePlugin(plugin());
            } catch (InvalidDescriptionException | InvalidPluginException e) {
                plugin().error("Error 25: Failed to load CrucialLib.");
                Bukkit.getPluginManager().disablePlugin(plugin());
            }
        } else if (!checkVersion(crucialLib)) {
            plugin().error("Error 26: Wrong version of CrucialLib.");
            plugin().getServer().getPluginManager().disablePlugin(crucialLib);
            try {
                download();
            } catch (IOException e) {
                plugin().error("Error 24: Failed to download CrucialLib");
                plugin().log("Please download it from: https://github.com/ChafficPlugins/CrucialLib/releases");
                Bukkit.getPluginManager().disablePlugin(plugin());
            } catch (InvalidPluginException | InvalidDescriptionException e) {
                plugin().error("Error 25: Failed to load CrucialLib.");
                Bukkit.getPluginManager().disablePlugin(plugin());
            }
        }
    }

    private static void download() throws IOException, InvalidPluginException, InvalidDescriptionException {
        plugin().log("Downloading CrucialLib");
        java.net.URL website = URI.create("https://github.com/ChafficPlugins/CrucialLib/releases/download/v" + CRUCIAL_LIB_VERSION + "/CrucialLib-v" + CRUCIAL_LIB_VERSION + ".jar").toURL();
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("plugins/CrucialLib.jar");
        fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
        plugin().log(ChatColor.GREEN + "Downloaded successfully.");
        Bukkit.getPluginManager().loadPlugin(new File("plugins/CrucialLib.jar"));
    }

    private static boolean checkVersion(Plugin crucialLib) {
        String version = crucialLib.getDescription().getVersion();
        if(version.equals(CRUCIAL_LIB_VERSION)) return true;
        String[] subVersions = version.split("\\.");
        String[] subVersions2 = CRUCIAL_LIB_VERSION.split("\\.");
        if(subVersions[0].equals(subVersions2[0]) && subVersions[1].equals(subVersions2[1])) {
            return Integer.parseInt(subVersions[2]) >= Integer.parseInt(subVersions2[2]);
        }
        return false;
    }

    public static boolean connect() throws IOException {
        Plugin crucialLib = plugin().getServer().getPluginManager().getPlugin("CrucialLib");
        if (crucialLib != null) {
            if (!crucialLib.isEnabled()) {
                Bukkit.getPluginManager().enablePlugin(crucialLib);
            }
            plugin().log(ChatColor.GREEN + "Successfully connected to CrucialLib.");
            isConnected = true;
            if (!crucialLib.getDescription().getVersion().startsWith(CRUCIAL_LIB_VERSION.substring(0,3))) {
                plugin().error("Error 24: Please update to CrucialLib " + CRUCIAL_LIB_VERSION);
                plugin().log("Please download it from: https://github.com/ChafficPlugins/CrucialLib/releases");
                return false;
            }
            return true;
        }
        plugin().error("Error 26: Failed to connect to CrucialLib.");
        return false;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }
}
