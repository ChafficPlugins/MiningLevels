package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {

    private static ServerMock server;

    @BeforeAll
    static void setUpServer() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void constructor_shouldStoreTypeAndAmount() {
        ItemStack item = new ItemStack(Material.DIAMOND, 10);
        Reward reward = new Reward(item);
        assertEquals(10, reward.getAmount());
    }

    @Test
    void getItemStack_shouldReturnCorrectMaterialAndAmount() {
        ItemStack item = new ItemStack(Material.IRON_INGOT, 5);
        Reward reward = new Reward(item);
        ItemStack result = reward.getItemStack();
        assertEquals(Material.IRON_INGOT, result.getType());
        assertEquals(5, result.getAmount());
    }

    @Test
    void getItemStack_singleItem_shouldHaveAmountOne() {
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        Reward reward = new Reward(item);
        assertEquals(1, reward.getAmount());
        assertEquals(Material.EMERALD, reward.getItemStack().getType());
    }
}
