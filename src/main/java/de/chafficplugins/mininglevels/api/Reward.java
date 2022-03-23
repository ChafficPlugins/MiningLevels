package de.chafficplugins.mininglevels.api;

import de.chafficplugins.mininglevels.MiningLevels;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Reward {
    private final static MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    private final ItemStack item = new ItemStack(Material.AIR);

    public Reward(String material, int amount) {
        Material m = Material.getMaterial(material);
        if(m == null) throw new IllegalArgumentException("Material " + material + " does not exist!");
        this.item.setType(m);
        this.item.setAmount(amount);
    }

    public ItemStack getItemStack() {
        return item;
    }

    public String getName() {
        return item.getType().name();
    }

    public int getAmount() {
        return item.getAmount();
    }
}
