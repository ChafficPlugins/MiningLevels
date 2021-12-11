package de.chaffic.advancedrpgmining.listeners.commands;

import de.chaffic.advancedrpgmining.api.MiningLevel;
import de.chaffic.advancedrpgmining.api.MiningPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                        player.sendMessage("You are now level " + level + ".");
                    }
                }
            } else {
                sender.sendMessage("Player " + args[1] + " does not exist.");
                return;
            }
        }
        sender.sendMessage("Usage: /armining setlevel <player> <level>");
    }

    public static void setXP(CommandSender sender, String[] args) {
        if(args.length == 3) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null) {
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer != null) {
                    int xp = Integer.parseInt(args[2]);
                    if(xp >= 0) {
                        miningPlayer.setXp(xp);
                        player.sendMessage("You got " + xp + " xp.");
                    }
                }
            } else {
                sender.sendMessage("Player " + args[1] + " does not exist.");
                return;
            }
        }
        sender.sendMessage("Usage: /armining setxp <player> <xp>");
    }

    public static void level(CommandSender sender, String[] args) {
        if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null) {
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer != null) {
                    sender.sendMessage(player.getDisplayName() + " is level " + miningPlayer.getLevel());
                }
            } else {
                sender.sendMessage("Player " + args[1] + " does not exist.");
                return;
            }
        }
        sender.sendMessage("Usage: /armining level <player>");
    }
}
