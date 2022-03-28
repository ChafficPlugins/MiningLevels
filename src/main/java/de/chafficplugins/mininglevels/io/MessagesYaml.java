package de.chafficplugins.mininglevels.io;

import de.chafficplugins.mininglevels.MiningLevels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class MessagesYaml {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    public static void create() throws IOException {
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messageFile.exists()) {
            messageFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }
        YamlConfiguration messages = YamlConfiguration.loadConfiguration(messageFile);
        messages.addDefault(NO_PERMISSION, "You don't have permission to do that!");
        messages.addDefault(NEW_LEVEL, "You are now level {0}");
        messages.addDefault(PLAYER_NOT_EXIST, "Player {0} does not exist!");
        messages.addDefault(XP_RECEIVED, "You received {0} XP.");
        messages.addDefault(LEVEL_OF, "{0} is level {1}.");
        messages.addDefault(LEVEL_UNLOCKED, "You've unlocked level {0}.");
        messages.addDefault(HASTELVL_CHANGE, "Haste Level: {0} -> {1}.");
        messages.addDefault(INSTANT_BREAK_CHANGE, "Instant Break Probability: {0} -> {1}.");
        messages.addDefault(EXTRA_ORE_CHANGE, "Extra Ore Probability: {0} -> {1}.");
        messages.addDefault(MAX_EXTRA_ORE_CHANGE, "Max Extra Ores: {0} -> {1}.");
        messages.addDefault(REWARDS_LIST, "Rewards:");
        messages.addDefault(CLAIM_YOUR_REWARD, "You can claim your reward by typing /miningrewards.");
        messages.addDefault(LEVEL_DROPPED, "Your level has dropped to {0}.");
        messages.addDefault(XP_GAINED, "Level {0}: [{1}/{2}]");
        messages.addDefault(RELOAD_SUCCESSFUL, "Successfully reloaded config.");
        messages.addDefault(ERROR_OCCURRED, "An error occurred.");
        messages.addDefault(NO_CONSOLE_COMMAND, "You can't execute this command from console.");
        messages.addDefault(CURRENT_LEVEL, "Current Level: {0}");
        messages.addDefault(CURRENT_XP, "Current XP: [{0}/{1}]");
        messages.addDefault(CURRENT_HASTE_LEVEL, "Current Haste Level: {0}");
        messages.addDefault(CURRENT_INSTANT_BREAK_LEVEL, "Current Instant Break Level: {0}");
        messages.addDefault(CURRENT_EXTRA_ORE_LEVEL, "Current Extra Ore Level: {0}");
        messages.addDefault(CURRENT_MAX_EXTRA_ORE, "Current Max Extra Ores: {0}");
        messages.addDefault(ONLY_BLOCKS_ALLOWED, "You can only add blocks to the material list.");
        messages.addDefault(CANT_DELETE_LEVEL, "You can only delete the last level.");
        messages.addDefault(NO_REWARDS, "You have no rewards.");
        messages.addDefault(REWARDS_CLAIMED, "You have claimed your reward.");
        messages.addDefault(NO_MORE_SPACE, "You don't have enough space in your inventory.");
        messages.addDefault(LEVEL_NEEDED, "You need to be level {0} to break this block.");
        messages.options().copyDefaults(true);
        messages.save(messageFile);
    }
}
