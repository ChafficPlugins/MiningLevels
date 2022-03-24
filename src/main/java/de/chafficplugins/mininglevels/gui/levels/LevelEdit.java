package de.chafficplugins.mininglevels.gui.levels;

import de.chafficplugins.mininglevels.api.MiningLevel;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.InventoryItem;
import io.github.chafficui.CrucialAPI.Utils.player.inventory.Page;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelEdit extends Page {
    private final MiningLevel level;

    public LevelEdit(MiningLevel level) {
        super(6, level.getName() + " Editor", Material.WHITE_STAINED_GLASS_PANE);
        this.isMovable = true;
        this.level = level;
    }

    @Override
    public void populate() {
        //0-2 nextXPEditor
        addItem(new InventoryItem(0, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            level.setNextLevelXP(level.getNextLevelXP() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(1, Material.EXPERIENCE_BOTTLE, "Next level xp", List.of(String.valueOf(level.getNextLevelXP())), inventoryClick -> {

        }));
        addItem(new InventoryItem(2, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            level.setNextLevelXP(level.getNextLevelXP() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //9-11 instantBreakEditor
        addItem(new InventoryItem(9, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            level.setInstantBreakProbability(level.getInstantBreakProbability() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(10, Material.STONE_BRICKS, "Instant break probability", List.of(String.valueOf(level.getInstantBreakProbability())), inventoryClick -> {

        }));
        addItem(new InventoryItem(11, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            level.setInstantBreakProbability(level.getInstantBreakProbability() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //18-20 extraOreProbabilityEditor
        addItem(new InventoryItem(18, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            level.setExtraOreProbability(level.getExtraOreProbability() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(19, Material.DIAMOND_ORE, "Extra ore probability", List.of(String.valueOf(level.getExtraOreProbability())), inventoryClick -> {

        }));
        addItem(new InventoryItem(20, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            level.setExtraOreProbability(level.getExtraOreProbability() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //27-29 extraOreAmountEditor
        addItem(new InventoryItem(27, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            level.setMaxExtraOre(level.getMaxExtraOre() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(28, Material.DIAMOND_BLOCK, "Maximum extra ores", List.of(String.valueOf(level.getMaxExtraOre())), inventoryClick -> {

        }));
        addItem(new InventoryItem(29, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            level.setMaxExtraOre(level.getMaxExtraOre() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //3-5 hasteLevelEditor
        addItem(new InventoryItem(3, Material.RED_STAINED_GLASS_PANE, "-1", Collections.emptyList(), inventoryClick -> {
            level.setHasteLevel(level.getHasteLevel() - 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        addItem(new InventoryItem(4, Material.GOLDEN_PICKAXE, "Haste level", List.of(String.valueOf(level.getHasteLevel())), inventoryClick -> {

        }));
        addItem(new InventoryItem(5, Material.GREEN_STAINED_GLASS_PANE, "+1", Collections.emptyList(), inventoryClick -> {
            level.setHasteLevel(level.getHasteLevel() + 1);
            reloadInventory();
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
        //49 Delete button
        addItem(new InventoryItem(49, Material.BARRIER, "Delete", Collections.emptyList(), inventoryClick -> {
            if(level.getOrdinal() == MiningLevel.miningLevels.size() - 1) {
                inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                MiningLevel.miningLevels.remove(level);
                inventoryClick.getPlayer().closeInventory();
                LevelList.getInstance().open(inventoryClick.getPlayer());
            } else {
                inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                inventoryClick.getPlayer().sendMessage(ChatColor.RED + "You can't delete a level that isn't the last one!");
            }
        }));
        //45 back button
        addItem(new InventoryItem(45, Material.YELLOW_STAINED_GLASS_PANE, "Back", Collections.emptyList(), inventoryClick -> {
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
            inventoryClick.getPlayer().closeInventory();
            LevelList.getInstance().open(inventoryClick.getPlayer());
        }));
        //7 Rewards
        addItem(new InventoryItem(7, Material.CHEST, "Rewards", Collections.emptyList(), inventoryClick -> {}));

        //16-44 reward items
        int[] rewardSlots = {16, 17, 25, 26, 34, 35, 43, 44};
        int currentItem = 0;
        for(int slot : rewardSlots) {
            if(level.getRewards() == null || level.getRewards().length <= currentItem) {
                addItem(new InventoryItem(slot, new ItemStack(Material.AIR), true));
            } else {
                addItem(new InventoryItem(slot, level.getRewards()[currentItem++], inventoryClick -> {}, true));
            }
        }

        //53 save items
        addItem(new InventoryItem(53, Material.GREEN_STAINED_GLASS_PANE, "Save rewards", Collections.emptyList(), inventoryClick -> {
            ArrayList<ItemStack> rewards = new ArrayList<>();
            for(int slot : rewardSlots) {
                ItemStack item = inventoryClick.getClickedInventory().getItem(slot);
                if(item != null && item.getType() != Material.AIR) {
                    rewards.add(item);
                }
            }
            level.setRewards(rewards);
            inventoryClick.getPlayer().playSound(inventoryClick.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            inventoryClick.getPlayer().closeInventory();
            LevelList.getInstance().open(inventoryClick.getPlayer());
        }));
    }
}
