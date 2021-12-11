package de.chaffic.advancedrpgmining.listeners;

import de.chaffic.advancedrpgmining.listeners.commands.LevelingCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("") && sender.isOp()) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "setlevel" -> {
                        LevelingCommands.setLevel(sender, args);
                    }
                    case "setxp" -> {
                        LevelingCommands.setXP(sender, args);
                    }
                    case "level" -> {
                        LevelingCommands.level(sender, args);
                    }
                    case "reload" -> {
                        //TODO: reload command
                    }
                    default -> {
                        sender.sendMessage("/armining help");
                        return true;
                    }
                }
            } else {
                sender.sendMessage("/armining help");
                return true;
            }
        }
        return false;
    }
}
