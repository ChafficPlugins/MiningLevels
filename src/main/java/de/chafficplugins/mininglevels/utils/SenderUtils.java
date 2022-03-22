package de.chafficplugins.mininglevels.utils;

import org.bukkit.command.CommandSender;

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
}
