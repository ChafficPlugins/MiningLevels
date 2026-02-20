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

    // --- constructor ---

    @Test
    void constructor_shouldStoreTypeAndAmount() {
        ItemStack item = new ItemStack(Material.DIAMOND, 10);
        Reward reward = new Reward(item);
        assertEquals(10, reward.getAmount());
    }

    @Test
    void constructor_singleItem_shouldHaveAmountOne() {
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        Reward reward = new Reward(item);
        assertEquals(1, reward.getAmount());
    }

    @Test
    void constructor_largeAmount_shouldStore() {
        ItemStack item = new ItemStack(Material.DIAMOND, 64);
        Reward reward = new Reward(item);
        assertEquals(64, reward.getAmount());
    }

    // --- getItemStack ---

    @Test
    void getItemStack_shouldReturnCorrectMaterialAndAmount() {
        ItemStack item = new ItemStack(Material.IRON_INGOT, 5);
        Reward reward = new Reward(item);
        ItemStack result = reward.getItemStack();
        assertEquals(Material.IRON_INGOT, result.getType());
        assertEquals(5, result.getAmount());
    }

    @Test
    void getItemStack_shouldReturnNewInstance() {
        ItemStack item = new ItemStack(Material.DIAMOND, 10);
        Reward reward = new Reward(item);
        ItemStack result1 = reward.getItemStack();
        ItemStack result2 = reward.getItemStack();
        // Each call returns a new instance
        assertNotSame(result1, result2);
        // But they should be equal in content
        assertEquals(result1.getType(), result2.getType());
        assertEquals(result1.getAmount(), result2.getAmount());
    }

    // --- different material types ---

    @Test
    void reward_diamond_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.DIAMOND, 3));
        assertEquals(Material.DIAMOND, reward.getItemStack().getType());
        assertEquals(3, reward.getAmount());
    }

    @Test
    void reward_ironIngot_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.IRON_INGOT, 16));
        assertEquals(Material.IRON_INGOT, reward.getItemStack().getType());
        assertEquals(16, reward.getAmount());
    }

    @Test
    void reward_goldIngot_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.GOLD_INGOT, 8));
        assertEquals(Material.GOLD_INGOT, reward.getItemStack().getType());
        assertEquals(8, reward.getAmount());
    }

    @Test
    void reward_emerald_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.EMERALD, 32));
        assertEquals(Material.EMERALD, reward.getItemStack().getType());
        assertEquals(32, reward.getAmount());
    }

    @Test
    void reward_netheriteIngot_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.NETHERITE_INGOT, 2));
        assertEquals(Material.NETHERITE_INGOT, reward.getItemStack().getType());
        assertEquals(2, reward.getAmount());
    }

    @Test
    void reward_tool_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.DIAMOND_PICKAXE, 1));
        assertEquals(Material.DIAMOND_PICKAXE, reward.getItemStack().getType());
        assertEquals(1, reward.getAmount());
    }

    @Test
    void reward_block_shouldPreserveMaterial() {
        Reward reward = new Reward(new ItemStack(Material.DIAMOND_BLOCK, 4));
        assertEquals(Material.DIAMOND_BLOCK, reward.getItemStack().getType());
        assertEquals(4, reward.getAmount());
    }

    // --- getAmount ---

    @Test
    void getAmount_shouldMatchConstructorAmount() {
        assertEquals(1, new Reward(new ItemStack(Material.DIAMOND, 1)).getAmount());
        assertEquals(16, new Reward(new ItemStack(Material.DIAMOND, 16)).getAmount());
        assertEquals(32, new Reward(new ItemStack(Material.DIAMOND, 32)).getAmount());
        assertEquals(64, new Reward(new ItemStack(Material.DIAMOND, 64)).getAmount());
    }

    // --- round-trip fidelity ---

    @Test
    void roundTrip_materialAndAmount_shouldBePreserved() {
        Material[] testMaterials = {
                Material.DIAMOND, Material.EMERALD, Material.IRON_INGOT,
                Material.GOLD_INGOT, Material.NETHERITE_INGOT, Material.COAL,
                Material.LAPIS_LAZULI, Material.REDSTONE
        };
        for (Material mat : testMaterials) {
            ItemStack original = new ItemStack(mat, 7);
            Reward reward = new Reward(original);
            ItemStack result = reward.getItemStack();
            assertEquals(mat, result.getType(), "Material mismatch for " + mat.name());
            assertEquals(7, result.getAmount(), "Amount mismatch for " + mat.name());
        }
    }
}
