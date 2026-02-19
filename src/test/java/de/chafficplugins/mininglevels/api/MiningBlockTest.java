package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

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

    @Test
    void constructor_singleMaterial_shouldCreateBlock() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        assertNotNull(block);
        assertEquals(1, block.getXp());
        assertEquals(0, block.getMinLevel());
        assertTrue(block.getMaterials().contains(Material.COAL_ORE));
    }

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
    void constructor_nonBlockMaterial_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () ->
                new MiningBlock(Material.DIAMOND, 1, 0));
    }

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
    void setXp_shouldUpdateValue() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setXp(10);
        assertEquals(10, block.getXp());
    }

    @Test
    void setMinLevel_validRange_shouldUpdateValue() {
        MiningBlock block = new MiningBlock(Material.COAL_ORE, 1, 0);
        block.setMinLevel(1);
        assertEquals(1, block.getMinLevel());
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
}
