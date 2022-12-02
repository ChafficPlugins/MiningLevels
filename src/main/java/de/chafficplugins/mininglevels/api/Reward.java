package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Reward {
    private final String type;
    private final int amount;
    ItemMeta meta;

    public Reward(ItemStack itemStack) {
        this.type = itemStack.getType().name();
        this.amount = itemStack.getAmount();
        this.meta = itemStack.getItemMeta();
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.valueOf(type), amount);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public int getAmount() {
        return amount;
    }
}
