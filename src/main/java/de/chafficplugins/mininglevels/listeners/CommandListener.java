package de.chafficplugins.mininglevels.listeners;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.listeners.commands.LevelingCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.chafficplugins.mininglevels.utils.SenderUtils.hasOnePermissions;

public class CommandListener implements CommandExecutor {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mininglevels") && sender.isOp()) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "setlevel" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setlevel")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return false;
                        }
                        LevelingCommands.setLevel(sender, args);
                    }
                    case "setxp" -> {
                        if(!hasOnePermissions(sender, "mininglevels.setxp")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return false;
                        }
                        LevelingCommands.setXP(sender, args);
                    }
                    case "level" -> {
                        if(!hasOnePermissions(sender, "mininglevels.level")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return false;
                        }
                        LevelingCommands.level(sender, args);
                    }
                    case "reload" -> {
                        if(!hasOnePermissions(sender, "mininglevels.reload")) {
                            sender.sendMessage("§cYou don't have the permission to do this!");
                            return false;
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
                    }
                    case "info" -> {
                        sender.sendMessage("§aMining Levels by §c" + plugin.getDescription().getAuthors());
                        sender.sendMessage("§aVersion: §c" + plugin.getDescription().getVersion());
                    }
                    case "self" -> {
                        if(!(sender instanceof Player)) {
                            sender.sendMessage("§cYou can't use this command from console!");
                            return false;
                        }
                        Player player = (Player) sender;
                        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                        if(miningPlayer == null) {
                            sender.sendMessage("§cAn error occurred. Please rejoin the server!");
                            return false;
                        }
                        player.sendMessage("§aYour current level is §c" + miningPlayer.getLevel().getName());
                        player.sendMessage("§aYour current XP is §c" + miningPlayer.getXp());
                    }
                    default -> {
                        if(hasOnePermissions(sender, "mininglevels.setlevel")) sender.sendMessage("§a/mininglevels setlevel <player> <level>");
                        if(hasOnePermissions(sender, "mininglevels.setxp")) sender.sendMessage("§a/mininglevels setxp <player> <xp>");
                        if(hasOnePermissions(sender, "mininglevels.level")) sender.sendMessage("§a/mininglevels level <player>");
                        if(hasOnePermissions(sender, "mininglevels.reload")) sender.sendMessage("§a/mininglevels reload");
                        sender.sendMessage("§a/mininglevels info");
                        sender.sendMessage("§a/mininglevels self");
                    }
                }
            } else {
                sender.sendMessage("/ml help");
            }
            return false;
        }
        return true;
    }
}
