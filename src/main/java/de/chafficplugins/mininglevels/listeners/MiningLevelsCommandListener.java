package de.chafficplugins.mininglevels.listeners;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.gui.levels.LevelList;
import de.chafficplugins.mininglevels.listeners.commands.LevelingCommands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

import static de.chafficplugins.mininglevels.utils.SenderUtils.hasOnePermissions;

public class MiningLevelsCommandListener implements CommandExecutor {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mininglevels")) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "setlevel" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setlevel")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return true;
                        }
                        LevelingCommands.setLevel(sender, args);
                        return true;
                    }
                    case "setxp" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setxp")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return true;
                        }
                        LevelingCommands.setXP(sender, args);
                        return true;
                    }
                    case "level" -> {
                        if(!hasOnePermissions(sender, "mininglevels.level")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return true;
                        }
                        LevelingCommands.level(sender, args);
                        return true;
                    }
                    case "reload" -> {
                        if(!hasOnePermissions(sender, "mininglevels.reload")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return true;
                        }
                        try {
                            MiningLevel.reload();
                            MiningBlock.reload();
                            MiningPlayer.reload();
                            sender.sendMessage("§aSuccessfully reloaded!");
                        } catch (IOException e) {
                            e.printStackTrace();
                            sender.sendMessage("§cAn error occurred while reloading!");
                        }
                        return true;
                    }
                    case "info" -> {
                        sender.sendMessage("§aMining Levels by §c" + plugin.getDescription().getAuthors());
                        sender.sendMessage("§aVersion: §c" + plugin.getDescription().getVersion());
                    }
                    case "self" -> {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage("§cYou can't use this command from console!");
                            return true;
                        }
                        return showLevelInfo(sender);
                    }
                    case "leveleditor" -> {
                        if(!hasOnePermissions(sender, "mininglevels.editor")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return true;
                        }
                        if(!(sender instanceof Player)) {
                            sender.sendMessage("§cYou can't use this command from console!");
                            return true;
                        }
                        LevelList.getInstance().open((Player) sender);
                    }
                    default -> {
                        sender.sendMessage("§a/mininglevels");
                        sender.sendMessage("§a/mininglevels info");
                        sender.sendMessage("§a/mininglevels self");
                        sender.sendMessage("§a/miningrewards");
                        if(hasOnePermissions(sender, "mininglevels.setlevel")) sender.sendMessage("§a/mininglevels setlevel <player> <level>");
                        if(hasOnePermissions(sender, "mininglevels.setxp")) sender.sendMessage("§a/mininglevels setxp <player> <xp>");
                        if(hasOnePermissions(sender, "mininglevels.level")) sender.sendMessage("§a/mininglevels level <player>");
                        if(hasOnePermissions(sender, "mininglevels.reload")) sender.sendMessage("§a/mininglevels reload");
                        return true;
                    }
                }
            } else {
                return showLevelInfo(sender);
            }
            return true;
        }
        return false;
    }

    private boolean showLevelInfo(CommandSender sender) {
        Player player = (Player) sender;
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
        if(miningPlayer == null) {
            sender.sendMessage("§cAn error occurred. Please rejoin the server!");
            return true;
        }
        MiningLevel level = miningPlayer.getLevel();

        player.sendMessage("§aYour current level is §c" + miningPlayer.getLevel().getName());
        player.sendMessage("§aYour current XP is: §c[" + miningPlayer.getXp() + "/" + level.getNextLevelXP() + "]");
        player.sendMessage(ChatColor.WHITE + "Haste Level: " + ChatColor.GREEN + level.getHasteLevel());
        player.sendMessage(ChatColor.WHITE + "Extra ore probability: " + ChatColor.GREEN + level.getExtraOreProbability());
        player.sendMessage(ChatColor.WHITE + "Max. extra ore amount: " + ChatColor.GREEN + level.getMaxExtraOre());
        player.sendMessage(ChatColor.WHITE + "Instant break probability: " + ChatColor.GREEN + level.getInstantBreakProbability());
        return true;
    }
}
