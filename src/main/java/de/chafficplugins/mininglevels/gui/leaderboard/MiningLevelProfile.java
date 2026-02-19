package de.chafficplugins.mininglevels.gui.leaderboard;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import io.github.chafficui.CrucialLib.Utils.customItems.Stack;
import io.github.chafficui.CrucialLib.Utils.localization.Localizer;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class MiningLevelProfile extends Page {

    OfflinePlayer player;
    Page lastPage;

    public MiningLevelProfile(OfflinePlayer player, Page lastPage) {
        super(1, "Profile", Material.WHITE_STAINED_GLASS_PANE);
        this.player = player;
        this.lastPage = lastPage;
    }

    public MiningLevelProfile(OfflinePlayer player) {
        super(1, "Profile", Material.WHITE_STAINED_GLASS_PANE);
        this.player = player;
        this.lastPage = null;
    }
    // 0 1 2 3 4 5 6 7 8

    @Override
    public void populate() {
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
        if (miningPlayer == null) return;
        ItemStack stack = Stack.getStack(miningPlayer.getUUID(), miningPlayer.getOfflinePlayer().getName(), List.of(
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_LEVEL, miningPlayer.getLevel().getName()),
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_XP, miningPlayer.getXp() + "", miningPlayer.getLevel().getNextLevelXP() + "")
        ));
        addItem(new InventoryItem(3, stack));

        addItem(new InventoryItem(5, Stack.getStack(Material.DIAMOND_PICKAXE, "-", List.of(
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_HASTE_LEVEL, miningPlayer.getLevel().getHasteLevel() + ""),
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_EXTRA_ORE_LEVEL, miningPlayer.getLevel().getExtraOreProbability() + ""),
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_MAX_EXTRA_ORE, miningPlayer.getLevel().getMaxExtraOre() + ""),
                Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CURRENT_INSTANT_BREAK_LEVEL, miningPlayer.getLevel().getInstantBreakProbability() + "")
        ))));

        addItem(new InventoryItem(0, Stack.getStack(Material.BARRIER, Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + CLOSE), null),
                (event) -> {
                    if (lastPage != null) {
                        lastPage.open(event.getPlayer());
                    } else {
                        event.getPlayer().closeInventory();
                    }
                }));
    }
}