package de.chafficplugins.mininglevels.listeners;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.gui.blocks.BlockList;
import de.chafficplugins.mininglevels.gui.leaderboard.MiningLevelProfile;
import de.chafficplugins.mininglevels.gui.levels.LevelList;
import de.chafficplugins.mininglevels.listeners.commands.LevelingCommands;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Locale;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.hasOnePermissions;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendMessage;

public class MiningLevelsCommandListener implements CommandExecutor {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mininglevels")) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "setlevel" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setlevel")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        LevelingCommands.setLevel(sender, args);
                        return true;
                    }
                    case "setxp" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setxp")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        LevelingCommands.setXP(sender, args);
                        return true;
                    }
                    case "level" -> {
                        if(!hasOnePermissions(sender, "mininglevels.level")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        LevelingCommands.level(sender, args);
                        return true;
                    }
                    case "leaderboard" -> {
                        if(!hasOnePermissions(sender, "mininglevels.leaderboard")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        LevelingCommands.leaderboard(sender);
                        return true;
                    }
                    case "reload" -> {
                        if(!hasOnePermissions(sender, "mininglevels.reload")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        try {
                            plugin.customMessages.reloadYaml();
                            plugin.reloadConfig();
                            MiningLevel.reload();
                            MiningBlock.reload();
                            try {
                                MiningLevels.lvlUpSound = Sound.valueOf(plugin.getConfigString(LVL_UP_SOUND).toUpperCase(Locale.ROOT));
                            } catch (NullPointerException | IllegalArgumentException e) {
                                plugin.error("Config value levelup sound is either misspelled or missing! Using ENTITY_PLAYER_LEVELUP");
                            }
                            PREFIX = plugin.getConfigString("prefix");
                            sendMessage(sender, RELOAD_SUCCESSFUL);
                        } catch (IOException e) {
                            e.printStackTrace();
                            sendMessage(sender, ERROR_OCCURRED);
                        }
                        return true;
                    }
                    case "info" -> {
                        if(!hasOnePermissions(sender, "mininglevels.info")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        LevelingCommands.info(sender);
                        return true;
                    }
                    case "self" -> {
                        if(!(sender instanceof Player)) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        return showLevelInfo(sender);
                    }
                    case "leveleditor" -> {
                        if(!hasOnePermissions(sender, "mininglevels.editor")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        if(!(sender instanceof Player)) {
                            sendMessage(sender, NO_CONSOLE_COMMAND);
                            return true;
                        }
                        LevelList.getInstance().open((Player) sender);
                    }
                    case "blockeditor" -> {
                        if(!hasOnePermissions(sender, "mininglevels.editor")) {
                            sendMessage(sender, NO_PERMISSION);
                            return true;
                        }
                        if(!(sender instanceof Player)) {
                            sendMessage(sender, NO_CONSOLE_COMMAND);
                            return true;
                        }
                        BlockList.getInstance().open((Player) sender);
                    }
                    default -> {
                        sender.sendMessage("§a/mininglevels");
                        sender.sendMessage("§a/mininglevels help");
                        sender.sendMessage("§a/mininglevels info");
                        sender.sendMessage("§a/mininglevels self");
                        sender.sendMessage("§a/miningrewards");
                        if(hasOnePermissions(sender, "mininglevels.leaderboard")) sender.sendMessage("§a/mininglevels leaderboard");
                        if(hasOnePermissions(sender, "mininglevels.setlevel")) sender.sendMessage("§a/mininglevels setlevel <player> <level>");
                        if(hasOnePermissions(sender, "mininglevels.setxp")) sender.sendMessage("§a/mininglevels setxp <player> <xp>");
                        if(hasOnePermissions(sender, "mininglevels.level")) sender.sendMessage("§a/mininglevels level <player>");
                        if(hasOnePermissions(sender, "mininglevels.reload")) sender.sendMessage("§a/mininglevels reload");
                        if(hasOnePermissions(sender, "mininglevels.editor")) sender.sendMessage("§a/mininglevels leveleditor");
                        if(hasOnePermissions(sender, "mininglevels.editor")) sender.sendMessage("§a/mininglevels blockeditor");
                        return true;
                    }
                }
            } else {
                if(sender instanceof Player) {
                    return showLevelInfo(sender);
                } else {
                    sender.sendMessage("§a/mininglevels help");
                }
            }
            return true;
        }
        return false;
    }

    private boolean showLevelInfo(CommandSender sender) {
        Player player = (Player) sender;
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
        if(miningPlayer == null) {
            sendMessage(sender, ERROR_OCCURRED);
            return true;
        }

        new MiningLevelProfile(player).open(player);

        return true;
    }
}
