package de.chafficplugins.mininglevels.utils;

import io.github.chafficui.CrucialAPI.Utils.localization.Localizer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.LOCALIZED_IDENTIFIER;
import static de.chafficplugins.mininglevels.utils.ConfigStrings.PREFIX;

public class SenderUtils {

    public static boolean hasOnePermissions(CommandSender sender, String... permissions) {
        if(sender.isOp() || sender.hasPermission(ConfigStrings.PERMISSION_ADMIN)) {
            return true;
        }
        for (String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    public static void sendMessage(CommandSender sender, String key, String... values) {
        sendMessage(sender, key, ChatColor.RESET, values);
    }

    public static void sendActionBar(Player sender, String key, String... values) {
        sendActionBar(sender, key, ChatColor.RESET, values);
    }

    public static void sendMessage(CommandSender sender, String key, ChatColor color, String... values) {
        sender.sendMessage(PREFIX + color + Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + key, values));
    }

    public static void sendActionBar(Player sender, String key, ChatColor color,  String... values) {
        sender.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color + Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + key, values)));
    }
}
