package de.chafficplugins.mininglevels.listeners;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendMessage;

public class RewardCommandListener  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("miningrewards")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                MiningPlayer miningPlayer = MiningPlayer.getOrCreateMiningPlayer(player.getUniqueId());
                switch (miningPlayer.claim()) {
                    case 0 -> sendMessage(player, NO_REWARDS, ChatColor.RED);
                    case 1 -> sendMessage(player, REWARDS_CLAIMED, ChatColor.GREEN);
                    case 2 -> sendMessage(player, NO_MORE_SPACE, ChatColor.YELLOW);
                }
                return true;
            } else {
                sendMessage(sender, NO_CONSOLE_COMMAND);
            }
        }
        return false;
    }
}
