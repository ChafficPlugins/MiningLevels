package de.chafficplugins.mininglevels.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderCommand {
    public static void perform(Player player, String command) {
        command = PlaceholderAPI.setPlaceholders(player, command);

        if(command.startsWith("console:")) {
            command = command.replace("console:", "");

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        } if(command.startsWith("player:")) {
            command = command.replace("player:", "");

            player.performCommand(command);
        }
    }
}
