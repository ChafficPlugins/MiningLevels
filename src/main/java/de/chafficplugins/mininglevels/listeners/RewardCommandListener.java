package de.chafficplugins.mininglevels.listeners;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RewardCommandListener  implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("miningrewards")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
                if(miningPlayer == null) {
                    return true;
                }
                switch (miningPlayer.claim()) {
                    case 0 -> player.sendMessage("§aThere are no rewards to claim!");
                    case 1 -> player.sendMessage("§aYou have claimed all rewards!");
                    case 2 -> player.sendMessage("§aThere is not enough space in you inventory to claim all rewards!");
                }
                return true;
            } else {
                sender.sendMessage("§cYou can only use this command as a player!");
            }
        }
        return false;
    }
}
