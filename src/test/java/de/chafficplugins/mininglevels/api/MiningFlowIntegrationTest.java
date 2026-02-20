package de.chafficplugins.mininglevels.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests that wire together MiningLevel, MiningBlock, and MiningPlayer
 * using the actual bundled default configurations, and test the full mining
 * progression flow.
 * <p>
 * Note: The actual {@code alterXp()}, {@code xpChange()}, and {@code levelUp()}
 * methods are coupled to the plugin instance and CrucialLib (for messaging,
 * sounds, and config). Since the plugin can't be loaded in tests (CrucialLib
 * isn't available), these tests replicate the core state-transition logic from
 * those methods to validate the domain integration.
 *
 * @see MiningPlayer#alterXp(int)
 * @see MiningLevel#levelUp(MiningPlayer)
 */
class MiningFlowIntegrationTest {

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
        MiningLevel.miningLevels.clear();
        MiningBlock.miningBlocks.clear();
        MiningPlayer.miningPlayers.clear();

        loadDefaultLevels();
        loadDefaultBlocks();
    }

    // ---- Deserialization integration: JSON → model classes ----

    @Test
    void defaultLevels_shouldDeserializeIntoMiningLevelObjects() {
        assertEquals(10, MiningLevel.miningLevels.size());
        for (int i = 0; i < MiningLevel.miningLevels.size(); i++) {
            MiningLevel level = MiningLevel.get(i);
            assertNotNull(level, "Level at ordinal " + i + " should exist");
            assertEquals(i, level.getOrdinal());
            assertNotNull(level.getName());
        }
    }

    @Test
    void defaultBlocks_shouldDeserializeIntoMiningBlockObjects() {
        assertEquals(9, MiningBlock.miningBlocks.size());
        for (MiningBlock block : MiningBlock.miningBlocks) {
            assertFalse(block.getMaterials().isEmpty(), "Each block should have at least one material");
            assertTrue(block.getXp() > 0, "Each block should have positive XP");
            assertTrue(block.getMinLevel() >= 0, "Each block should have non-negative minLevel");
        }
    }

    @Test
    void defaultLevels_shouldHaveIncreasingXpThresholds() {
        int prevXp = 0;
        for (MiningLevel level : MiningLevel.miningLevels) {
            assertTrue(level.getNextLevelXP() > prevXp,
                    "Level " + level.getName() + " XP (" + level.getNextLevelXP()
                            + ") should exceed previous (" + prevXp + ")");
            prevXp = level.getNextLevelXP();
        }
    }

    @Test
    void defaultLevels_skillsShouldProgressMonotonically() {
        float prevInstant = -1, prevExtra = -1;
        int prevMaxOre = -1, prevHaste = -1;
        for (MiningLevel level : MiningLevel.miningLevels) {
            assertTrue(level.getInstantBreakProbability() >= prevInstant,
                    "instantBreakProbability should not decrease at level " + level.getName());
            assertTrue(level.getExtraOreProbability() >= prevExtra,
                    "extraOreProbability should not decrease at level " + level.getName());
            assertTrue(level.getMaxExtraOre() >= prevMaxOre,
                    "maxExtraOre should not decrease at level " + level.getName());
            assertTrue(level.getHasteLevel() >= prevHaste,
                    "hasteLevel should not decrease at level " + level.getName());
            prevInstant = level.getInstantBreakProbability();
            prevExtra = level.getExtraOreProbability();
            prevMaxOre = level.getMaxExtraOre();
            prevHaste = level.getHasteLevel();
        }
    }

    // ---- Block lookup integration ----

    @Test
    void getMiningBlock_shouldFindAllDefaultMaterials() {
        Material[] expectedMaterials = {
                Material.STONE, Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE,
                Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE,
                Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE,
                Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE,
                Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE,
                Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE,
                Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE,
                Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE
        };
        for (Material mat : expectedMaterials) {
            assertNotNull(MiningBlock.getMiningBlock(mat),
                    "Should find a MiningBlock for " + mat.name());
        }
    }

    @Test
    void getMiningBlock_regularAndDeepslateVariants_shouldShareSameBlock() {
        MiningBlock iron = MiningBlock.getMiningBlock(Material.IRON_ORE);
        MiningBlock deepslateIron = MiningBlock.getMiningBlock(Material.DEEPSLATE_IRON_ORE);
        assertNotNull(iron);
        assertSame(iron, deepslateIron, "Regular and deepslate iron ore should be the same MiningBlock");

        MiningBlock diamond = MiningBlock.getMiningBlock(Material.DIAMOND_ORE);
        MiningBlock deepslateDiamond = MiningBlock.getMiningBlock(Material.DEEPSLATE_DIAMOND_ORE);
        assertSame(diamond, deepslateDiamond, "Regular and deepslate diamond ore should be the same MiningBlock");
    }

    @Test
    void getMiningBlock_nonMiningBlock_shouldReturnNull() {
        assertNull(MiningBlock.getMiningBlock(Material.DIRT));
        assertNull(MiningBlock.getMiningBlock(Material.GRASS_BLOCK));
        assertNull(MiningBlock.getMiningBlock(Material.OAK_LOG));
    }

    // ---- Level requirement gating ----

    @Test
    void levelGating_level0Player_shouldOnlyAccessLevel0Blocks() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        Set<Material> accessible = getAccessibleMaterials(player);

        // Level 0 blocks: STONE (minLevel 0), REDSTONE_ORE (0), COAL_ORE (0)
        assertTrue(accessible.contains(Material.STONE));
        assertTrue(accessible.contains(Material.COAL_ORE));
        assertTrue(accessible.contains(Material.REDSTONE_ORE));

        // Level 1+ blocks should be inaccessible
        assertFalse(accessible.contains(Material.IRON_ORE));
        assertFalse(accessible.contains(Material.GOLD_ORE));
        assertFalse(accessible.contains(Material.DIAMOND_ORE));
    }

    @Test
    void levelGating_level2Player_shouldAccessLevel0Through2Blocks() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 2, 0);

        // minLevel 0
        assertTrue(canMine(player, Material.STONE));
        assertTrue(canMine(player, Material.COAL_ORE));
        // minLevel 1
        assertTrue(canMine(player, Material.IRON_ORE));
        // minLevel 2
        assertTrue(canMine(player, Material.GOLD_ORE));
        assertTrue(canMine(player, Material.COPPER_ORE));
        // minLevel 3 — too high
        assertFalse(canMine(player, Material.LAPIS_ORE));
        assertFalse(canMine(player, Material.EMERALD_ORE));
        // minLevel 5 — too high
        assertFalse(canMine(player, Material.DIAMOND_ORE));
    }

    @Test
    void levelGating_maxLevelPlayer_shouldAccessAllBlocks() {
        int maxOrdinal = MiningLevel.getMaxLevel().getOrdinal();
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), maxOrdinal, 0);

        for (MiningBlock block : MiningBlock.miningBlocks) {
            for (Material mat : block.getMaterials()) {
                assertTrue(canMine(player, mat),
                        "Max level player should be able to mine " + mat.name());
            }
        }
    }

    // ---- XP accumulation and level-up flow ----

    @Test
    void miningFlow_coalOre_shouldAccumulateXp() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        MiningBlock coal = MiningBlock.getMiningBlock(Material.COAL_ORE);
        assertNotNull(coal);

        simulateMining(player, coal);
        assertEquals(10, player.getXp()); // coal gives 10 XP

        simulateMining(player, coal);
        assertEquals(20, player.getXp());
    }

    @Test
    void miningFlow_shouldLevelUpWhenXpReachesThreshold() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        assertEquals(0, player.getLevel().getOrdinal());

        // Level 1 requires 100 XP. Coal gives 10 XP. Mine 10 coal ores.
        MiningBlock coal = MiningBlock.getMiningBlock(Material.COAL_ORE);
        for (int i = 0; i < 10; i++) {
            simulateMining(player, coal);
        }
        // Should have leveled up to ordinal 1, with overflow XP = 0
        assertEquals(1, player.getLevel().getOrdinal(), "Should have leveled up to ordinal 1");
        assertEquals(0, player.getXp(), "Overflow XP should be 0 (exactly 100 XP)");
    }

    @Test
    void miningFlow_shouldCarryOverflowXpAfterLevelUp() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 95);
        // 95 XP + 10 XP from coal = 105 XP, threshold is 100 → level up, overflow = 5
        MiningBlock coal = MiningBlock.getMiningBlock(Material.COAL_ORE);
        simulateMining(player, coal);

        assertEquals(1, player.getLevel().getOrdinal());
        assertEquals(5, player.getXp(), "Should carry 5 XP overflow into next level");
    }

    @Test
    void miningFlow_highXpBlock_shouldCauseMultiLevelJump() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        // Diamond gives 1000 XP.
        // Level 0→1: 100 XP (overflow 900)
        // Level 1→2: 300 XP (overflow 600)
        // Level 2→3: 600 XP (overflow 0)
        // But player can't mine diamond at level 0 (minLevel 5), so use gold instead.
        // Gold gives 100 XP. Start at level 2 (minLevel 2 for gold).
        // Level 2→3: need 600 XP. But that requires mining 6 gold ores.
        // Let's just test with manual XP to verify multi-level jump logic.
        player.setXp(0);
        player.setLevel(0);

        // Simulate getting 1000 XP at once (e.g., admin command or multiple blocks)
        addXpWithLevelUp(player, 1000);

        // 1000 XP should cascade: 100→300→600 = levels 0,1,2 consumed, overflow 0
        assertEquals(3, player.getLevel().getOrdinal(),
                "1000 XP from level 0 should reach ordinal 3");
        assertEquals(0, player.getXp(), "Should have 0 overflow after consuming exactly 1000 XP");
    }

    @Test
    void miningFlow_exactlyEnoughXpForOneLevel_shouldLevelUpWithZeroOverflow() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        addXpWithLevelUp(player, 100); // exactly level 0 threshold

        assertEquals(1, player.getLevel().getOrdinal());
        assertEquals(0, player.getXp());
    }

    @Test
    void miningFlow_oneLessXpThanNeeded_shouldNotLevelUp() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        addXpWithLevelUp(player, 99);

        assertEquals(0, player.getLevel().getOrdinal());
        assertEquals(99, player.getXp());
    }

    @Test
    void miningFlow_atMaxLevel_shouldNotLevelUpFurther() {
        int maxOrdinal = MiningLevel.getMaxLevel().getOrdinal();
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), maxOrdinal, 0);
        addXpWithLevelUp(player, 999999);

        assertEquals(maxOrdinal, player.getLevel().getOrdinal(), "Should stay at max level");
        assertEquals(999999, player.getXp(), "XP should accumulate without leveling");
    }

    // ---- Progressive unlock through mining ----

    @Test
    void progressiveUnlock_miningCoalShouldEventuallyUnlockIronOre() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        MiningBlock coal = MiningBlock.getMiningBlock(Material.COAL_ORE);
        assertFalse(canMine(player, Material.IRON_ORE), "Should not mine iron at level 0");

        // Mine coal until level 1 (iron requires minLevel 1)
        // Level 0 threshold: 100 XP, coal gives 10 XP → 10 coal ores
        for (int i = 0; i < 10; i++) {
            simulateMining(player, coal);
        }

        assertEquals(1, player.getLevel().getOrdinal());
        assertTrue(canMine(player, Material.IRON_ORE), "Should mine iron at level 1");
    }

    @Test
    void progressiveUnlock_shouldUnlockBlocksAtCorrectLevels() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);

        // Track which level each block becomes accessible
        // minLevel 0: STONE, COAL_ORE, REDSTONE_ORE
        // minLevel 1: IRON_ORE
        // minLevel 2: GOLD_ORE, COPPER_ORE
        // minLevel 3: LAPIS_ORE, EMERALD_ORE
        // minLevel 5: DIAMOND_ORE

        int[] thresholds = {100, 300, 600, 1000, 2000, 6000};
        Material[][] unlocksByLevel = {
                {}, // nothing new at level 0→1 transition besides iron
                {Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE},
                {Material.GOLD_ORE, Material.COPPER_ORE},
                {Material.LAPIS_ORE, Material.EMERALD_ORE},
                {}, // nothing new at level 4
                {Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE}
        };

        for (int targetLevel = 1; targetLevel <= 5; targetLevel++) {
            // Advance to target level
            while (player.getLevel().getOrdinal() < targetLevel) {
                addXpWithLevelUp(player, player.getLevel().getNextLevelXP() - player.getXp());
            }

            for (Material mat : unlocksByLevel[targetLevel]) {
                assertTrue(canMine(player, mat),
                        mat.name() + " should be accessible at level " + targetLevel);
            }
        }
    }

    // ---- Full progression simulation ----

    @Test
    void fullProgression_shouldReachMaxLevelByMiningEnoughBlocks() {
        MiningPlayer player = new MiningPlayer(UUID.randomUUID(), 0, 0);
        int maxOrdinal = MiningLevel.getMaxLevel().getOrdinal();

        // Progress through all levels using available blocks
        while (player.getLevel().getOrdinal() < maxOrdinal) {
            // Find the highest-XP block the player can mine
            MiningBlock bestBlock = getBestMineableBlock(player);
            assertNotNull(bestBlock,
                    "Player at level " + player.getLevel().getOrdinal()
                            + " should always have at least one mineable block");
            simulateMining(player, bestBlock);
        }

        assertEquals(maxOrdinal, player.getLevel().getOrdinal());
    }

    @Test
    void fullProgression_levelNavigationShouldWorkThroughEntireChain() {
        // Test that getNext()/getBefore() chains work across all default levels
        MiningLevel first = MiningLevel.get(0);
        assertNotNull(first);

        // Walk forward
        MiningLevel current = first;
        for (int i = 1; i < MiningLevel.miningLevels.size(); i++) {
            MiningLevel next = current.getNext();
            assertEquals(i, next.getOrdinal(), "getNext() from ordinal " + (i - 1));
            current = next;
        }
        // At max level, getNext() should return self
        assertSame(current, current.getNext(), "getNext() at max level should return self");

        // Walk backward
        for (int i = MiningLevel.miningLevels.size() - 2; i >= 0; i--) {
            MiningLevel before = current.getBefore();
            assertEquals(i, before.getOrdinal(), "getBefore() from ordinal " + (i + 1));
            current = before;
        }
        // At min level, getBefore() should return self
        assertSame(current, current.getBefore(), "getBefore() at min level should return self");
    }

    // ---- Multi-player scenarios ----

    @Test
    void multiPlayer_independentProgression() {
        MiningPlayer alice = new MiningPlayer(UUID.randomUUID(), 0, 0);
        MiningPlayer bob = new MiningPlayer(UUID.randomUUID(), 0, 0);

        MiningBlock coal = MiningBlock.getMiningBlock(Material.COAL_ORE);
        // Alice mines 10 coal (100 XP → level 1)
        for (int i = 0; i < 10; i++) {
            simulateMining(alice, coal);
        }

        assertEquals(1, alice.getLevel().getOrdinal());
        assertEquals(0, bob.getLevel().getOrdinal(), "Bob should still be level 0");
        assertEquals(0, bob.getXp(), "Bob should still have 0 XP");
    }

    @Test
    void multiPlayer_lookupByUUID_shouldReturnCorrectPlayer() {
        UUID aliceId = UUID.randomUUID();
        UUID bobId = UUID.randomUUID();
        MiningPlayer alice = new MiningPlayer(aliceId, 0, 0);
        MiningPlayer bob = new MiningPlayer(bobId, 3, 500);

        assertSame(alice, MiningPlayer.getMiningPlayer(aliceId));
        assertSame(bob, MiningPlayer.getMiningPlayer(bobId));
        assertEquals(0, MiningPlayer.getMiningPlayer(aliceId).getLevel().getOrdinal());
        assertEquals(3, MiningPlayer.getMiningPlayer(bobId).getLevel().getOrdinal());
    }

    // ---- XP values from defaults ----

    @Test
    void defaultXpValues_shouldMatchExpectedHierarchy() {
        // Diamond > Gold > Iron > Coal/Emerald > Copper > Stone/Redstone/Lapis
        int diamondXp = MiningBlock.getMiningBlock(Material.DIAMOND_ORE).getXp();
        int goldXp = MiningBlock.getMiningBlock(Material.GOLD_ORE).getXp();
        int ironXp = MiningBlock.getMiningBlock(Material.IRON_ORE).getXp();
        int coalXp = MiningBlock.getMiningBlock(Material.COAL_ORE).getXp();
        int copperXp = MiningBlock.getMiningBlock(Material.COPPER_ORE).getXp();
        int stoneXp = MiningBlock.getMiningBlock(Material.STONE).getXp();

        assertTrue(diamondXp > goldXp, "Diamond should give more XP than gold");
        assertTrue(goldXp > ironXp, "Gold should give more XP than iron");
        assertTrue(ironXp > coalXp, "Iron should give more XP than coal");
        assertTrue(coalXp > copperXp, "Coal should give more XP than copper");
        assertTrue(copperXp > stoneXp, "Copper should give more XP than stone");
    }

    @Test
    void defaultBlocks_allMinLevelsShouldMapToExistingLevels() {
        for (MiningBlock block : MiningBlock.miningBlocks) {
            MiningLevel requiredLevel = MiningLevel.get(block.getMinLevel());
            assertNotNull(requiredLevel,
                    "Block with minLevel " + block.getMinLevel()
                            + " should map to an existing MiningLevel");
        }
    }

    // ---- Helpers ----

    /**
     * Simulates the core mining flow: checks level requirement, adds XP, and
     * triggers level-up if threshold is reached. Replicates the state-transition
     * logic from {@code MiningEvents.onBlockBreak()}, {@code MiningPlayer.alterXp()},
     * {@code MiningPlayer.xpChange()}, and {@code MiningLevel.levelUp()}.
     *
     * @return true if the block was mined, false if level was too low
     */
    private boolean simulateMining(MiningPlayer player, MiningBlock block) {
        // Level check (from MiningEvents.onBlockBreak, line 76)
        if (player.getLevel().getOrdinal() < block.getMinLevel()) {
            return false;
        }
        // XP addition (from MiningPlayer.alterXp, line 118)
        addXpWithLevelUp(player, block.getXp());
        return true;
    }

    /**
     * Adds XP and processes level-ups. Replicates the logic from
     * {@code MiningPlayer.xpChange()} (line 149) and
     * {@code MiningLevel.levelUp()} (lines 240-242).
     */
    private void addXpWithLevelUp(MiningPlayer player, int xp) {
        player.setXp(player.getXp() + xp);
        // Level-up loop (from xpChange line 149 + levelUp lines 240-242)
        while (player.getXp() >= player.getLevel().getNextLevelXP()
                && player.getLevel().getOrdinal() + 1 < MiningLevel.miningLevels.size()) {
            int overflow = player.getXp() - player.getLevel().getNextLevelXP();
            player.setLevel(player.getLevel().getNext());
            player.setXp(overflow);
        }
    }

    private boolean canMine(MiningPlayer player, Material material) {
        MiningBlock block = MiningBlock.getMiningBlock(material);
        return block != null && player.getLevel().getOrdinal() >= block.getMinLevel();
    }

    private Set<Material> getAccessibleMaterials(MiningPlayer player) {
        Set<Material> accessible = new HashSet<>();
        for (MiningBlock block : MiningBlock.miningBlocks) {
            if (player.getLevel().getOrdinal() >= block.getMinLevel()) {
                accessible.addAll(block.getMaterials());
            }
        }
        return accessible;
    }

    private MiningBlock getBestMineableBlock(MiningPlayer player) {
        MiningBlock best = null;
        for (MiningBlock block : MiningBlock.miningBlocks) {
            if (player.getLevel().getOrdinal() >= block.getMinLevel()) {
                if (best == null || block.getXp() > best.getXp()) {
                    best = block;
                }
            }
        }
        return best;
    }

    private void loadDefaultLevels() {
        try (InputStream is = getClass().getResourceAsStream("/defaults/levels.json")) {
            assertNotNull(is, "levels.json not found on classpath");
            try (Reader reader = new InputStreamReader(is)) {
                ArrayList<MiningLevel> levels = new Gson().fromJson(reader,
                        new TypeToken<ArrayList<MiningLevel>>() {}.getType());
                MiningLevel.miningLevels.addAll(levels);
            }
        } catch (IOException e) {
            fail("Failed to load levels.json: " + e.getMessage());
        }
    }

    private void loadDefaultBlocks() {
        try (InputStream is = getClass().getResourceAsStream("/defaults/blocks.json")) {
            assertNotNull(is, "blocks.json not found on classpath");
            try (Reader reader = new InputStreamReader(is)) {
                ArrayList<MiningBlock> blocks = new Gson().fromJson(reader,
                        new TypeToken<ArrayList<MiningBlock>>() {}.getType());
                MiningBlock.miningBlocks.addAll(blocks);
            }
        } catch (IOException e) {
            fail("Failed to load blocks.json: " + e.getMessage());
        }
    }
}
