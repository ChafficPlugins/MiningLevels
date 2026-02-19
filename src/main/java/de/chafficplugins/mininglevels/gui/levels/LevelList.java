package de.chafficplugins.mininglevels.gui.levels;

import de.chafficplugins.mininglevels.api.MiningLevel;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

/**
 * A GUI that lists all levels, lets you edit, delete and create new levels.
 */
public class LevelList extends Page {
    /**
     * The current instance.
     */
    private static LevelList instance;

    /**
     * Gets the instance of the LevelList.
     * @return The instance of the LevelList.
     */
    public static LevelList getInstance() {
        if(instance == null) {
            instance = new LevelList();
        }
        return instance;
    }

    private LevelList() {
        super(6, "Levels", Material.WHITE_STAINED_GLASS_PANE);
    }

    @Override
    public void populate() {
        int slot = 0;
        for (MiningLevel level : MiningLevel.miningLevels) {
            addItem(new InventoryItem(slot++, Material.WITHER_SKELETON_SKULL, level.getName(), List.of(
                    "§7Ordinal: §e" + level.getOrdinal(),
                    "§7Exp to next level: §e" + level.getNextLevelXP()
            ), (inventoryClick) -> {
                new LevelEdit(level).open(inventoryClick.getPlayer());
                inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
            }));
        }
        addItem(new InventoryItem(slot, Material.PLAYER_HEAD, "Create new level",
                List.of("§7Click to create"),
                (inventoryClick) -> {
                    int nextOrdinal = MiningLevel.miningLevels.size();
                    MiningLevel level = new MiningLevel(String.valueOf(nextOrdinal+1), 100, nextOrdinal);
                    MiningLevel.miningLevels.add(level);
                    reloadInventory();
                    inventoryClick.getPlayer().closeInventory();
                    new LevelEdit(level).open(inventoryClick.getPlayer());
                    inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
        ));
    }
}
