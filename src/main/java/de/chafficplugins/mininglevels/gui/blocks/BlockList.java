package de.chafficplugins.mininglevels.gui.blocks;

import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import io.github.chafficui.CrucialLib.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialLib.Utils.player.inventory.Page;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;

public class BlockList extends Page {
    /**
     * The current instance.
     */
    private static BlockList instance;

    /**
     * Gets the instance of the BlockList.
     * @return The instance of the BlockList.
     */
    public static BlockList getInstance() {
        if(instance == null) {
            instance = new BlockList();
        }
        return instance;
    }

    public BlockList() {
        super(6, "Blocks", Material.WHITE_STAINED_GLASS_PANE);
    }

    @Override
    public void populate() {
        int slot = 0;
        for (MiningBlock block : MiningBlock.miningBlocks) {
            MiningLevel level = MiningLevel.get(block.getMinLevel());
            if(level == null) continue;
            addItem(new InventoryItem(slot++, block.getMaterials().get(0), block.getMaterials().get(0).name(), List.of(
                    "§xp: §e" + block.getXp(),
                    "§7min. level: §e" + level.getName()
            ), (inventoryClick) -> {
                new BlockEdit(block).open(inventoryClick.getPlayer());
                inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
            }));
        }
        addItem(new InventoryItem(slot, Material.PLAYER_HEAD, "Create new Block",
                List.of("§7Click to create"),
                (inventoryClick) -> {
                    MiningBlock block = new MiningBlock(Material.DIAMOND_BLOCK, 0, 0);
                    MiningBlock.miningBlocks.add(block);
                    reloadInventory();
                    inventoryClick.getPlayer().closeInventory();
                    new BlockEdit(block).open(inventoryClick.getPlayer());
                    inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
        ));
    }
}
