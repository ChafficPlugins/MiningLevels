package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MiningBlockTest {

    @BeforeAll
    static void setUpServer() {
        MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void setUp() {
        MiningBlock.miningBlocks.clear();
        MiningLevel.miningLevels.clear();
        MiningLevel.miningLevels.add(new MiningLevel("Beginner", 100, 0));
        MiningLevel.miningLevels.add(new MiningLevel("Apprentice", 300, 1));
        MiningLevel.miningLevels.add(new MiningLevel("Expert", 500, 2));
    }

    // --- single-material constructor ---

    @Test
    void constructor_singleMaterial_shouldCreateBlock() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        assertNotNull(block);
        assertEquals(1, block.getXp());
        assertEquals(0, block.getMinLevel());
        assertTrue(block.getMaterials().contains(Material.COAL_ORE));
    }

    @Test
    void constructor_singleMaterial_shouldHaveOneMaterial() {
        MiningBlock block = new MiningBlock(Material.IRON_ORE, 3, 1);
        assertEquals(1, block.getMaterials().size());
    }

    @Test
    void constructor_differentBlockTypes_shouldWork() {
        assertDoesNotThrow(() -> new MiningBlock(Material.STONE, 1, 0));
        assertDoesNotThrow(() -> new MiningBlock(Material.DIAMOND_ORE, 5, 0));
        assertDoesNotThrow(() -> new MiningBlock(Material.DEEPSLATE_GOLD_ORE, 4, 0));
        assertDoesNotThrow(() -> new MiningBlock(Material.NETHERRACK, 1, 0));
        assertDoesNotThrow(() -> new MiningBlock(Material.OBSIDIAN, 2, 0));
    }

    // --- multi-material constructor ---

    @Test
    void constructor_multipleMaterials_shouldCreateBlock() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE},
                2, 0
        );
        assertEquals(2, block.getMaterials().size());
        assertTrue(block.getMaterials().contains(Material.REDSTONE_ORE));
        assertTrue(block.getMaterials().contains(Material.DEEPSLATE_REDSTONE_ORE));
    }

    @Test
    void constructor_threeMaterials_shouldCreateBlock() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.STONE},
                1, 0
        );
        assertEquals(3, block.getMaterials().size());
    }

    @Test
    void constructor_singleMaterialArray_shouldWork() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.DIAMOND_ORE}, 5, 2
        );
        assertEquals(1, block.getMaterials().size());
        assertTrue(block.getMaterials().contains(Material.DIAMOND_ORE));
    }

    // --- non-block material rejection ---

    @Test
    void constructor_nonBlockMaterial_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(Material.DIAMOND, 1, 0));
    }

    @Test
    void constructor_nonBlockMaterials_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(new Material[]{Material.DIAMOND_SWORD}, 1, 0));
    }

    @Test
    void constructor_mixedBlockAndNonBlock_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(
                        new Material[]{Material.COAL_ORE, Material.DIAMOND},
                        1, 0
                ));
    }

    @Test
    void constructor_itemMaterial_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(Material.IRON_INGOT, 1, 0));
    }

    @Test
    void constructor_toolMaterial_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(Material.DIAMOND_PICKAXE, 1, 0));
    }

    // --- getMiningBlock ---

    @Test
    void getMiningBlock_existingMaterial_shouldReturnBlock() {
        MiningBlock block = new MiningBlock(Material.DIAMOND_ORE, 5, 2);
        MiningBlock.miningBlocks.add(block);

        MiningBlock found = MiningBlock.getMiningBlock(Material.DIAMOND_ORE);
        assertNotNull(found);
        assertEquals(5, found.getXp());
        assertEquals(2, found.getMinLevel());
    }

    @Test
    void getMiningBlock_nonExistingMaterial_shouldReturnNull() {
        MiningBlock.miningBlocks.add(new MiningBlock(Material.COAL_ORE, 1, 0));

        assertNull(MiningBlock.getMiningBlock(Material.DIAMOND_ORE));
    }

    @Test
    void getMiningBlock_fromMultiMaterialBlock_shouldReturnBlock() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE},
                3, 1
        );
        MiningBlock.miningBlocks.add(block);

        assertNotNull(MiningBlock.getMiningBlock(Material.IRON_ORE));
        assertNotNull(MiningBlock.getMiningBlock(Material.DEEPSLATE_IRON_ORE));
        assertEquals(block, MiningBlock.getMiningBlock(Material.IRON_ORE));
    }

    @Test
    void getMiningBlock_emptyList_shouldReturnNull() {
        assertNull(MiningBlock.getMiningBlock(Material.COAL_ORE));
    }

    @Test
    void getMiningBlock_multipleBlocks_shouldFindCorrectOne() {
        MiningBlock coal = new MiningBlock(Material.COAL_ORE, 1, 0);
        MiningBlock iron = new MiningBlock(Material.IRON_ORE, 3, 1);
        MiningBlock diamond = new MiningBlock(Material.DIAMOND_ORE, 5, 2);
        MiningBlock.miningBlocks.add(coal);
        MiningBlock.miningBlocks.add(iron);
        MiningBlock.miningBlocks.add(diamond);

        MiningBlock found = MiningBlock.getMiningBlock(Material.IRON_ORE);
        assertNotNull(found);
        assertEquals(3, found.getXp());
        assertEquals(1, found.getMinLevel());
    }

    // --- setXp ---

    @Test
    void setXp_shouldUpdateValue() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setXp(10);
        assertEquals(10, block.getXp());
    }

    @Test
    void setXp_toZero_shouldWork() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 5, 0);
        block.setXp(0);
        assertEquals(0, block.getXp());
    }

    @Test
    void setXp_toLargeValue_shouldWork() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setXp(99999);
        assertEquals(99999, block.getXp());
    }

    @Test
    void setXp_multipleTimes_shouldOverwrite() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setXp(5);
        assertEquals(5, block.getXp());
        block.setXp(15);
        assertEquals(15, block.getXp());
        block.setXp(1);
        assertEquals(1, block.getXp());
    }

    // --- setMinLevel ---

    @Test
    void setMinLevel_validRange_shouldUpdateValue() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setMinLevel(1);
        assertEquals(1, block.getMinLevel());
    }

    @Test
    void setMinLevel_maxValidOrdinal_shouldWork() {
        // miningLevels.size() is 3, so max valid is 2 (< 3 and > 0)
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 1);
        block.setMinLevel(2);
        assertEquals(2, block.getMinLevel());
    }

    @Test
    void setMinLevel_outOfRange_shouldNotChange() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 1);
        // 0 is not > 0, so setMinLevel(0) won't change
        block.setMinLevel(0);
        assertEquals(1, block.getMinLevel());
        // 10 >= miningLevels.size() (3), so won't change
        block.setMinLevel(10);
        assertEquals(1, block.getMinLevel());
    }

    @Test
    void setMinLevel_exactlySize_shouldNotChange() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 1);
        block.setMinLevel(3); // equals size, condition is < size
        assertEquals(1, block.getMinLevel());
    }

    @Test
    void setMinLevel_negative_shouldNotChange() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 1);
        block.setMinLevel(-1);
        assertEquals(1, block.getMinLevel());
    }

    // --- setMaterials ---

    @Test
    void setMaterials_shouldReplaceMaterials() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        assertEquals(1, block.getMaterials().size());
        assertTrue(block.getMaterials().contains(Material.COAL_ORE));

        ArrayList<ItemStack> newMaterials = new ArrayList<>();
        newMaterials.add(new ItemStack(Material.IRON_ORE));
        newMaterials.add(new ItemStack(Material.GOLD_ORE));
        block.setMaterials(newMaterials);

        assertEquals(2, block.getMaterials().size());
        assertTrue(block.getMaterials().contains(Material.IRON_ORE));
        assertTrue(block.getMaterials().contains(Material.GOLD_ORE));
        assertFalse(block.getMaterials().contains(Material.COAL_ORE));
    }

    @Test
    void setMaterials_emptyList_shouldClearMaterials() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setMaterials(new ArrayList<>());
        assertEquals(0, block.getMaterials().size());
    }

    @Test
    void setMaterials_singleItem_shouldWork() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE},
                1, 0
        );
        ArrayList<ItemStack> newMaterials = new ArrayList<>();
        newMaterials.add(new ItemStack(Material.DIAMOND_ORE));
        block.setMaterials(newMaterials);

        assertEquals(1, block.getMaterials().size());
        assertTrue(block.getMaterials().contains(Material.DIAMOND_ORE));
    }

    // --- multiple blocks in registry ---

    @Test
    void multipleMiningBlocks_shouldTrackSeparately() {
        MiningBlock coal = new MiningBlock(Material.COAL_ORE, 1, 0);
        MiningBlock iron = new MiningBlock(Material.IRON_ORE, 3, 1);
        MiningBlock diamond = new MiningBlock(Material.DIAMOND_ORE, 5, 2);

        MiningBlock.miningBlocks.add(coal);
        MiningBlock.miningBlocks.add(iron);
        MiningBlock.miningBlocks.add(diamond);

        assertEquals(3, MiningBlock.miningBlocks.size());
        assertEquals(coal, MiningBlock.getMiningBlock(Material.COAL_ORE));
        assertEquals(iron, MiningBlock.getMiningBlock(Material.IRON_ORE));
        assertEquals(diamond, MiningBlock.getMiningBlock(Material.DIAMOND_ORE));
    }

    @Test
    void multipleMultiMaterialBlocks_shouldTrackSeparately() {
        MiningBlock redstone = new MiningBlock(
                new Material[]{Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE}, 2, 0
        );
        MiningBlock lapis = new MiningBlock(
                new Material[]{Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE}, 3, 1
        );
        MiningBlock.miningBlocks.add(redstone);
        MiningBlock.miningBlocks.add(lapis);

        assertEquals(redstone, MiningBlock.getMiningBlock(Material.REDSTONE_ORE));
        assertEquals(redstone, MiningBlock.getMiningBlock(Material.DEEPSLATE_REDSTONE_ORE));
        assertEquals(lapis, MiningBlock.getMiningBlock(Material.LAPIS_ORE));
        assertEquals(lapis, MiningBlock.getMiningBlock(Material.DEEPSLATE_LAPIS_ORE));
    }

    // --- XP and level stored correctly after construction ---

    @Test
    void constructor_xpAndLevel_shouldBeStoredCorrectly() {
        MiningBlock block = new MiningBlock(Material.EMERALD_ORE, 7, 2);
        assertEquals(7, block.getXp());
        assertEquals(2, block.getMinLevel());
        assertTrue(block.getMaterials().contains(Material.EMERALD_ORE));
    }

    @Test
    void constructor_zeroXp_shouldWork() {
        MiningBlock block = new MiningBlock(Material.STONE, 0, 0);
        assertEquals(0, block.getXp());
    }

    @Test
    void constructor_zeroMinLevel_shouldWork() {
        MiningBlock block = new MiningBlock(Material.STONE, 1, 0);
        assertEquals(0, block.getMinLevel());
    }

    // --- getMaterials returns new list ---

    @Test
    void getMaterials_shouldReturnCorrectMaterialTypes() {
        MiningBlock block = new MiningBlock(
                new Material[]{Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE},
                2, 0
        );
        ArrayList<Material> materials = block.getMaterials();
        assertEquals(2, materials.size());
        assertTrue(materials.contains(Material.COPPER_ORE));
        assertTrue(materials.contains(Material.DEEPSLATE_COPPER_ORE));
    }
}
