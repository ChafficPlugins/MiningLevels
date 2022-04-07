package de.chafficplugins.mininglevels.gui.leaderboard;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LeaderboardList extends Page {

    public LeaderboardList() {
        super(6, "Leaderboard", Material.WHITE_STAINED_GLASS_PANE);
    }

    @Override
    public void populate() {
        List<MiningPlayer> miningPlayers = MiningPlayer.getSortedPlayers();

        for (int i = 0; i < 53; i++) {
            if (i < miningPlayers.size()) {
                MiningPlayer miningPlayer = miningPlayers.get(i);
                if (miningPlayer == null) continue;
                ItemStack stack = Stack.getStack(miningPlayer.getUUID(), i + 1 + ". " + miningPlayer.getOfflinePlayer().getName(), List.of(
                        "§7Level: §e" + miningPlayer.getLevel().getName(),
                        "§7XP: §e" + miningPlayer.getXp() + " / " + miningPlayer.getLevel().getNextLevelXP()
                ));

                addItem(new InventoryItem(i, stack));
            }
        }
    }
}
