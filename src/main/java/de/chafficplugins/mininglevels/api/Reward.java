package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0.0
 *
 * Defines a reward with an ItemStack and some usefull methods.
 */
public class Reward {

    /**
     * The ItemStack of the reward.
     */
    private final ItemStack item;

    /**
     * Creates a new Reward with the given settings.
     * @param material The Material of the reward.
     * @param amount The amount of the reward.
     */
    public Reward(String material, int amount) {
        this.item = new ItemStack(Material.AIR);
        Material m = Material.getMaterial(material);
        if(m == null) throw new IllegalArgumentException("Material " + material + " does not exist!");
        this.item.setType(m);
        this.item.setAmount(amount);
    }

    /**
     * Creates a new Reward with the given ItemStack.
     * @param item The ItemStack of the reward.
     */
    public Reward(ItemStack item) {
        this.item = item;
    }

    /**
     * Returns the ItemStack of the reward.
     * @return The ItemStack of the reward.
     */
    public ItemStack getItemStack() {
        return item;
    }

    /**
     * Returns the name of the reward if defined. Otherwise the Material name is returned.
     * @return The name of the reward. If the name is not defined, the material will be returned.
     */
    public String getName() {
        if(item.getItemMeta() != null) {
            return item.getItemMeta().getDisplayName();
        } else {
            return item.getType().name();
        }
    }

    /**
     * Returns the amount of the reward.
     * @return The amount of the reward.
     */
    public int getAmount() {
        return item.getAmount();
    }
}
