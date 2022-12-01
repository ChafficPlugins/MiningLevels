package de.chafficplugins.mininglevels.gui.leaderboard;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import io.github.chafficui.CrucialAPI.Utils.customItems.Stack;
import io.github.chafficui.CrucialAPI.Utils.localization.Localizer;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class MiningLevelProfile extends Page {

    public MiningLevelProfile() {
        super(1, "Profile", Material.WHITE_STAINED_GLASS_PANE);
    }
    // 0 1 2 3 4 5 6 7 8

    @Override
    public void populate() {
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(getInventory().getViewers().get(0).getUniqueId());
        if (miningPlayer == null) return;
        ItemStack stack = Stack.getStack(miningPlayer.getUUID(), miningPlayer.getOfflinePlayer().getName(), List.of(
                Localizer.getLocalizedString(CURRENT_LEVEL, miningPlayer.getLevel().getName()),
                Localizer.getLocalizedString(CURRENT_XP, miningPlayer.getXp() + "", miningPlayer.getLevel().getNextLevelXP() + "")
        ));
        addItem(new InventoryItem(3, stack));

        addItem(new InventoryItem(5, Stack.getStack(Material.DIAMOND_PICKAXE, "", List.of(
                Localizer.getLocalizedString(CURRENT_HASTE_LEVEL, miningPlayer.getLevel().getHasteLevel() + ""),
                Localizer.getLocalizedString(CURRENT_EXTRA_ORE_LEVEL, miningPlayer.getLevel().getExtraOreProbability() + ""),
                Localizer.getLocalizedString(CURRENT_MAX_EXTRA_ORE, miningPlayer.getLevel().getMaxExtraOre() + ""),
                Localizer.getLocalizedString(CURRENT_INSTANT_BREAK_LEVEL, miningPlayer.getLevel().getInstantBreakProbability() + "")
        ))));

        addItem(new InventoryItem(8, Stack.getStack(Material.BARRIER, Localizer.getLocalizedString(CLOSE), null),
                (event) -> event.getPlayer().closeInventory()));
    }
}