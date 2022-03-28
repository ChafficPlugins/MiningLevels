package de.chafficplugins.mininglevels.listeners.commands;

import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendMessage;

public class LevelingCommands {
    public static void setLevel(CommandSender sender, String[] args) {
        if(args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null) {
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer != null) {
                    int level = Integer.parseInt(args[2]);
                    if(level >= 0 && level < MiningLevel.miningLevels.size()) {
                        miningPlayer.setLevel(level);
                        sendMessage(player, NEW_LEVEL, String.valueOf(level));
                        return;
                    }
                }
            } else {
                sendMessage(sender, PLAYER_NOT_EXIST, args[1]);
                return;
            }
        }
        sendMessage(sender, USAGE_SET_LEVEL);
    }

    public static void setXP(CommandSender sender, String[] args) {
        if(args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null) {
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer != null) {
                    int xp = Integer.parseInt(args[2]);
                    if(xp >= 0) {
                        miningPlayer.changeXp(xp);
                        sendMessage(player, XP_RECEIVED, String.valueOf(xp));
                        return;
                    }
                }
            } else {
                sendMessage(sender, PLAYER_NOT_EXIST, args[1]);
                return;
            }
        }
        sendMessage(sender, USAGE_SET_XP);
    }

    public static void level(CommandSender sender, String[] args) {
        if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null) {
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer != null) {
                    sendMessage(sender, LEVEL_OF, player.getDisplayName(), String.valueOf(miningPlayer.getLevel().getName()));
                    return;
                }
            } else {
                sendMessage(sender, PLAYER_NOT_EXIST, args[1]);
                return;
            }
        }
        sendMessage(sender, USAGE_LEVEL);
    }
}
