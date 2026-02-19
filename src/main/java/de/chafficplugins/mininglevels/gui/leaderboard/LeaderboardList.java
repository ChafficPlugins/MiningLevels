package de.chafficplugins.mininglevels.gui.leaderboard;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import io.github.chafficui.CrucialLib.Utils.localization.Localizer;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

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
                        Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_LEVEL, miningPlayer.getLevel().getName()),
                        Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_XP, miningPlayer.getXp() + "", miningPlayer.getLevel().getNextLevelXP() + "")
                ));

                addItem(new InventoryItem(i, stack, (event) -> new MiningLevelProfile(miningPlayer.getOfflinePlayer(), this).open(event.getPlayer())));
            }
        }
    }
}
