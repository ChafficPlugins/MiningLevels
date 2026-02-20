package de.chafficplugins.mininglevels.listeners.commands;

import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.gui.leaderboard.LeaderboardList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static de.chafficplugins.mininglevels.api.MiningPlayer.getSortedPlayers;
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

    public static void info(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            sendMessage(sender, NO_CONSOLE_COMMAND);
            return;
        }
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
        if(miningPlayer == null) {
            sendMessage(sender, ERROR_OCCURRED);
            return;
        }
        MiningLevel miningLevel = miningPlayer.getLevel();
        sendMessage(sender, CURRENT_LEVEL, miningLevel.getName());
        sendMessage(sender, CURRENT_XP, String.valueOf(miningPlayer.getXp()), String.valueOf(miningLevel.getNextLevelXP()));
    }

    public static void leaderboard(CommandSender sender) {
        //sort miningPlayers by level and xp
        List<MiningPlayer> miningPlayers = getSortedPlayers();

        if(sender instanceof Player) {
            Player player = (Player) sender;

            new LeaderboardList().open(player);

            return;
        }
        sendMessage(sender, LEADERBOARD_HEADER);

        for(int i = 0; i < 5; i++) {
            if(i < miningPlayers.size()) {
                MiningPlayer miningPlayer = miningPlayers.get(i);
                if(miningPlayer == null) break;
                sender.sendMessage(PREFIX + ChatColor.YELLOW + (i + 1) + ChatColor.RESET + ". " + ChatColor.GREEN + miningPlayer.getOfflinePlayer().getName() + ChatColor.RESET + " | " + miningPlayer.getLevel().getName() + " (" + miningPlayer.getXp() + "xp)");
            }
        }
    }
}
