package de.chafficplugins.mininglevels.gui.blocks;

import de.chafficplugins.mininglevels.api.MiningBlock;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockEdit extends Page {
    private final MiningBlock block;

    public BlockEdit(MiningBlock block) {
        super(6, "MiningBlock Editor", Material.WHITE_STAINED_GLASS_PANE);
        this.isMovable = true;
        this.block = block;
    }

    @Override
    public void populate() {
        //0-2 minLevelEditor
        addItem(new InventoryItem(0, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            block.setMinLevel(block.getMinLevel() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(1, Material.EXPERIENCE_BOTTLE, "Min. level", List.of(String.valueOf(block.getMinLevel()), "Ordinal number of the level."), inventoryClick -> {

        }));
        addItem(new InventoryItem(2, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            block.setMinLevel(block.getMinLevel() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //9-11 droppedXPEditor
        addItem(new InventoryItem(9, Material.RED_STAINED_GLASS_PANE, "-10", Collections.emptyList(), inventoryClick -> {
            block.setXp(block.getXp() - 10);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(10, Material.STONE_BRICKS, "Dropped xp", List.of(String.valueOf(block.getXp()), "Xp amount dropped, when the block is destroyed."), inventoryClick -> {

        }));
        addItem(new InventoryItem(11, Material.GREEN_STAINED_GLASS_PANE, "+10", Collections.emptyList(), inventoryClick -> {
            block.setXp(block.getXp() + 10);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));

        //49 Delete button
        addItem(new InventoryItem(49, Material.BARRIER, "Delete", Collections.emptyList(), inventoryClick -> {
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            MiningBlock.miningBlocks.remove(block);
        }));
        //45 back button
        addItem(new InventoryItem(45, Material.YELLOW_STAINED_GLASS_PANE, "Back", Collections.emptyList(), inventoryClick -> {
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
            inventoryClick.getPlayer().closeInventory();
            BlockList.getInstance().open(inventoryClick.getPlayer());
        }));
        //7 Blocks
        addItem(new InventoryItem(7, Material.CHEST, "Materials", List.of("All materials with this xp amount and min. level"), inventoryClick -> {}));

        //16-44 reward items
        int[] blockSlots = {16, 17, 25, 26, 34, 35, 43, 44};
        int currentItem = 0;
        for(int slot : blockSlots) {
            if(block.getMaterials() == null || block.getMaterials().size() <= currentItem) {
                addItem(new InventoryItem(slot, new ItemStack(Material.AIR), true));
            } else {
                addItem(new InventoryItem(slot, new ItemStack(block.getMaterials().get(currentItem++)), inventoryClick -> {}, true));
            }
        }

        //53 save items
        addItem(new InventoryItem(53, Material.GREEN_STAINED_GLASS_PANE, "Save materials", Collections.emptyList(), inventoryClick -> {
            ArrayList<ItemStack> blocks = new ArrayList<>();
            for(int slot : blockSlots) {
                ItemStack item = inventoryClick.getClickedInventory().getItem(slot);
                if(item != null && item.getType() != Material.AIR) {
                    if(item.getType().isBlock()) {
                        blocks.add(item);
                    } else {
                        inventoryClick.getPlayer().sendMessage(ChatColor.RED + "You can only add blocks to the materials list.");
                        inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    }
                }
            }
            block.setMaterials(blocks);
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            inventoryClick.getPlayer().closeInventory();
            BlockList.getInstance().open(inventoryClick.getPlayer());
        }));
    }
}
