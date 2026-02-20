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

class MiningLevelTest {

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
        MiningLevel level0 = new MiningLevel("Beginner", 100, 0);
        MiningLevel level1 = new MiningLevel("Apprentice", 300, 1);
        MiningLevel level2 = new MiningLevel("Expert", 500, 2);
        MiningLevel level3 = new MiningLevel("Master", 1000, 3);
        MiningLevel.miningLevels.add(level0);
        MiningLevel.miningLevels.add(level1);
        MiningLevel.miningLevels.add(level2);
        MiningLevel.miningLevels.add(level3);
    }

    // --- get() ---

    @Test
    void get_shouldReturnCorrectLevel() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals("Beginner", level.getName());
        assertEquals(0, level.getOrdinal());
    }

    @Test
    void get_eachOrdinal_shouldReturnCorrectLevel() {
        assertEquals("Beginner", MiningLevel.get(0).getName());
        assertEquals("Apprentice", MiningLevel.get(1).getName());
        assertEquals("Expert", MiningLevel.get(2).getName());
        assertEquals("Master", MiningLevel.get(3).getName());
    }

    @Test
    void get_shouldReturnNullForInvalidOrdinal() {
        assertNull(MiningLevel.get(99));
        assertNull(MiningLevel.get(-1));
    }

    @Test
    void get_shouldReturnNullForOrdinalJustOutOfRange() {
        assertNull(MiningLevel.get(4));
    }

    @Test
    void get_emptyList_shouldReturnNull() {
        MiningLevel.miningLevels.clear();
        assertNull(MiningLevel.get(0));
    }

    // --- getNext() ---

    @Test
    void getNext_shouldReturnNextLevel() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        MiningLevel next = level.getNext();
        assertNotNull(next);
        assertEquals("Apprentice", next.getName());
        assertEquals(1, next.getOrdinal());
    }

    @Test
    void getNext_atMaxLevel_shouldReturnSelf() {
        MiningLevel maxLevel = MiningLevel.get(3);
        assertNotNull(maxLevel);
        MiningLevel next = maxLevel.getNext();
        assertEquals(maxLevel, next);
    }

    @Test
    void getNext_chainThroughAllLevels() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals("Beginner", level.getName());

        level = level.getNext();
        assertEquals("Apprentice", level.getName());

        level = level.getNext();
        assertEquals("Expert", level.getName());

        level = level.getNext();
        assertEquals("Master", level.getName());

        // at max, getNext returns self
        level = level.getNext();
        assertEquals("Master", level.getName());
    }

    @Test
    void getNext_secondToLast_shouldReturnLast() {
        MiningLevel expert = MiningLevel.get(2);
        assertNotNull(expert);
        assertEquals("Master", expert.getNext().getName());
    }

    // --- getBefore() ---

    @Test
    void getBefore_shouldReturnPreviousLevel() {
        MiningLevel level = MiningLevel.get(2);
        assertNotNull(level);
        MiningLevel before = level.getBefore();
        assertNotNull(before);
        assertEquals("Apprentice", before.getName());
        assertEquals(1, before.getOrdinal());
    }

    @Test
    void getBefore_atMinLevel_shouldReturnSelf() {
        MiningLevel minLevel = MiningLevel.get(0);
        assertNotNull(minLevel);
        MiningLevel before = minLevel.getBefore();
        assertEquals(minLevel, before);
    }

    @Test
    void getBefore_chainThroughAllLevels() {
        MiningLevel level = MiningLevel.get(3);
        assertNotNull(level);
        assertEquals("Master", level.getName());

        level = level.getBefore();
        assertEquals("Expert", level.getName());

        level = level.getBefore();
        assertEquals("Apprentice", level.getName());

        level = level.getBefore();
        assertEquals("Beginner", level.getName());

        // at min, getBefore returns self
        level = level.getBefore();
        assertEquals("Beginner", level.getName());
    }

    @Test
    void getBefore_secondLevel_shouldReturnFirst() {
        MiningLevel apprentice = MiningLevel.get(1);
        assertNotNull(apprentice);
        assertEquals("Beginner", apprentice.getBefore().getName());
    }

    // --- getNextLevelXP / setNextLevelXP ---

    @Test
    void getNextLevelXP_shouldReturnCorrectValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(100, level.getNextLevelXP());
    }

    @Test
    void getNextLevelXP_eachLevel_shouldReturnCorrectValue() {
        assertEquals(100, MiningLevel.get(0).getNextLevelXP());
        assertEquals(300, MiningLevel.get(1).getNextLevelXP());
        assertEquals(500, MiningLevel.get(2).getNextLevelXP());
        assertEquals(1000, MiningLevel.get(3).getNextLevelXP());
    }

    @Test
    void setNextLevelXP_shouldUpdateValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setNextLevelXP(200);
        assertEquals(200, level.getNextLevelXP());
    }

    @Test
    void setNextLevelXP_toZero_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setNextLevelXP(0);
        assertEquals(0, level.getNextLevelXP());
    }

    @Test
    void setNextLevelXP_toLargeValue_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setNextLevelXP(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, level.getNextLevelXP());
    }

    // --- getMaxLevel ---

    @Test
    void getMaxLevel_shouldReturnLastLevel() {
        MiningLevel maxLevel = MiningLevel.getMaxLevel();
        assertNotNull(maxLevel);
        assertEquals("Master", maxLevel.getName());
        assertEquals(3, maxLevel.getOrdinal());
    }

    @Test
    void getMaxLevel_singleLevel_shouldReturnThatLevel() {
        MiningLevel.miningLevels.clear();
        MiningLevel only = new MiningLevel("Only", 100, 0);
        MiningLevel.miningLevels.add(only);
        assertEquals(only, MiningLevel.getMaxLevel());
    }

    @Test
    void getMaxLevel_twoLevels_shouldReturnSecond() {
        MiningLevel.miningLevels.clear();
        MiningLevel first = new MiningLevel("First", 100, 0);
        MiningLevel second = new MiningLevel("Second", 200, 1);
        MiningLevel.miningLevels.add(first);
        MiningLevel.miningLevels.add(second);
        assertEquals(second, MiningLevel.getMaxLevel());
    }

    // --- equals ---

    @Test
    void equals_sameLevels_shouldBeEqual() {
        MiningLevel level1 = MiningLevel.get(0);
        MiningLevel level2 = MiningLevel.get(0);
        assertEquals(level1, level2);
    }

    @Test
    void equals_differentLevels_shouldNotBeEqual() {
        MiningLevel level1 = MiningLevel.get(0);
        MiningLevel level2 = MiningLevel.get(1);
        assertNotEquals(level1, level2);
    }

    @Test
    void equals_null_shouldReturnFalse() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertNotEquals(level, null);
    }

    @Test
    void equals_differentType_shouldReturnFalse() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertNotEquals(level, "not a level");
        assertNotEquals(level, 42);
    }

    @Test
    void equals_sameOrdinalDifferentName_shouldBeEqual() {
        MiningLevel.miningLevels.clear();
        MiningLevel a = new MiningLevel("LevelA", 100, 0);
        MiningLevel b = new MiningLevel("LevelB", 200, 0);
        // equals is based on ordinal only
        assertEquals(a, b);
    }

    // --- instantBreakProbability ---

    @Test
    void instantBreakProbability_defaultIsZero() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(0, level.getInstantBreakProbability());
    }

    @Test
    void setInstantBreakProbability_validRange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setInstantBreakProbability(50);
        assertEquals(50, level.getInstantBreakProbability());
    }

    @Test
    void setInstantBreakProbability_zero_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setInstantBreakProbability(50);
        level.setInstantBreakProbability(0);
        assertEquals(0, level.getInstantBreakProbability());
    }

    @Test
    void setInstantBreakProbability_hundred_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setInstantBreakProbability(100);
        assertEquals(100, level.getInstantBreakProbability());
    }

    @Test
    void setInstantBreakProbability_outOfRange_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setInstantBreakProbability(50);
        level.setInstantBreakProbability(101);
        assertEquals(50, level.getInstantBreakProbability());
        level.setInstantBreakProbability(-1);
        assertEquals(50, level.getInstantBreakProbability());
    }

    @Test
    void setInstantBreakProbability_fractional_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setInstantBreakProbability(33.5f);
        assertEquals(33.5f, level.getInstantBreakProbability());
    }

    // --- extraOreProbability ---

    @Test
    void extraOreProbability_defaultIsZero() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(0, level.getExtraOreProbability());
    }

    @Test
    void setExtraOreProbability_validRange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setExtraOreProbability(25);
        assertEquals(25, level.getExtraOreProbability());
    }

    @Test
    void setExtraOreProbability_zero_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setExtraOreProbability(50);
        level.setExtraOreProbability(0);
        assertEquals(0, level.getExtraOreProbability());
    }

    @Test
    void setExtraOreProbability_hundred_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setExtraOreProbability(100);
        assertEquals(100, level.getExtraOreProbability());
    }

    @Test
    void setExtraOreProbability_outOfRange_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setExtraOreProbability(25);
        level.setExtraOreProbability(101);
        assertEquals(25, level.getExtraOreProbability());
        level.setExtraOreProbability(-1);
        assertEquals(25, level.getExtraOreProbability());
    }

    @Test
    void setExtraOreProbability_fractional_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setExtraOreProbability(12.75f);
        assertEquals(12.75f, level.getExtraOreProbability());
    }

    // --- maxExtraOre ---

    @Test
    void maxExtraOre_defaultIsZero() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(0, level.getMaxExtraOre());
    }

    @Test
    void setMaxExtraOre_validValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setMaxExtraOre(5);
        assertEquals(5, level.getMaxExtraOre());
    }

    @Test
    void setMaxExtraOre_zero_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setMaxExtraOre(5);
        level.setMaxExtraOre(0);
        assertEquals(0, level.getMaxExtraOre());
    }

    @Test
    void setMaxExtraOre_negative_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setMaxExtraOre(5);
        level.setMaxExtraOre(-1);
        assertEquals(5, level.getMaxExtraOre());
    }

    @Test
    void setMaxExtraOre_largeValue_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setMaxExtraOre(999);
        assertEquals(999, level.getMaxExtraOre());
    }

    // --- hasteLevel ---

    @Test
    void hasteLevel_defaultIsZero() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(0, level.getHasteLevel());
    }

    @Test
    void setHasteLevel_validValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setHasteLevel(3);
        assertEquals(3, level.getHasteLevel());
    }

    @Test
    void setHasteLevel_zero_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setHasteLevel(3);
        level.setHasteLevel(0);
        assertEquals(0, level.getHasteLevel());
    }

    @Test
    void setHasteLevel_negative_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setHasteLevel(3);
        level.setHasteLevel(-1);
        assertEquals(3, level.getHasteLevel());
    }

    @Test
    void setHasteLevel_largeValue_shouldWork() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setHasteLevel(255);
        assertEquals(255, level.getHasteLevel());
    }

    // --- rewards ---

    @Test
    void getRewards_default_shouldBeEmpty() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        ItemStack[] rewards = level.getRewards();
        assertNotNull(rewards);
        assertEquals(0, rewards.length);
    }

    @Test
    void addReward_singleItem_shouldBeRetrievable() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.addReward(new ItemStack(Material.DIAMOND, 5));
        ItemStack[] rewards = level.getRewards();
        assertEquals(1, rewards.length);
        assertEquals(Material.DIAMOND, rewards[0].getType());
        assertEquals(5, rewards[0].getAmount());
    }

    @Test
    void addReward_multipleItems_shouldAllBeRetrievable() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.addReward(new ItemStack(Material.DIAMOND, 5));
        level.addReward(new ItemStack(Material.IRON_INGOT, 10));
        level.addReward(new ItemStack(Material.GOLD_INGOT, 3));

        ItemStack[] rewards = level.getRewards();
        assertEquals(3, rewards.length);
        assertEquals(Material.DIAMOND, rewards[0].getType());
        assertEquals(Material.IRON_INGOT, rewards[1].getType());
        assertEquals(Material.GOLD_INGOT, rewards[2].getType());
    }

    @Test
    void setRewards_shouldReplaceExisting() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.addReward(new ItemStack(Material.DIAMOND, 5));
        assertEquals(1, level.getRewards().length);

        ArrayList<ItemStack> newRewards = new ArrayList<>();
        newRewards.add(new ItemStack(Material.EMERALD, 20));
        newRewards.add(new ItemStack(Material.NETHERITE_INGOT, 1));
        level.setRewards(newRewards);

        ItemStack[] rewards = level.getRewards();
        assertEquals(2, rewards.length);
        assertEquals(Material.EMERALD, rewards[0].getType());
        assertEquals(20, rewards[0].getAmount());
        assertEquals(Material.NETHERITE_INGOT, rewards[1].getType());
    }

    @Test
    void setRewards_emptyList_shouldClearRewards() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.addReward(new ItemStack(Material.DIAMOND, 5));
        assertEquals(1, level.getRewards().length);

        level.setRewards(new ArrayList<>());
        assertEquals(0, level.getRewards().length);
    }

    // --- getName(Reward) ---

    @Test
    void getName_normalItem_shouldReturnMaterialName() {
        Reward reward = new Reward(new ItemStack(Material.DIAMOND, 1));
        assertEquals("DIAMOND", MiningLevel.getName(reward));
    }

    @Test
    void getName_differentMaterials_shouldReturnCorrectNames() {
        assertEquals("IRON_INGOT", MiningLevel.getName(new Reward(new ItemStack(Material.IRON_INGOT, 1))));
        assertEquals("GOLD_INGOT", MiningLevel.getName(new Reward(new ItemStack(Material.GOLD_INGOT, 1))));
        assertEquals("EMERALD", MiningLevel.getName(new Reward(new ItemStack(Material.EMERALD, 1))));
    }

    // --- constructor ---

    @Test
    void constructor_shouldStoreNameAndOrdinal() {
        MiningLevel level = new MiningLevel("TestLevel", 500, 99);
        assertEquals("TestLevel", level.getName());
        assertEquals(500, level.getNextLevelXP());
        assertEquals(99, level.getOrdinal());
    }

    @Test
    void constructor_emptyName_shouldWork() {
        MiningLevel level = new MiningLevel("", 100, 0);
        assertEquals("", level.getName());
    }

    @Test
    void constructor_zeroXP_shouldWork() {
        MiningLevel level = new MiningLevel("Zero", 0, 0);
        assertEquals(0, level.getNextLevelXP());
    }

    // --- list size ---

    @Test
    void levelListSize_shouldBeFour() {
        assertEquals(4, MiningLevel.miningLevels.size());
    }

    // --- navigation with single level ---

    @Test
    void singleLevel_getNext_shouldReturnSelf() {
        MiningLevel.miningLevels.clear();
        MiningLevel only = new MiningLevel("Only", 100, 0);
        MiningLevel.miningLevels.add(only);
        assertEquals(only, only.getNext());
    }

    @Test
    void singleLevel_getBefore_shouldReturnSelf() {
        MiningLevel.miningLevels.clear();
        MiningLevel only = new MiningLevel("Only", 100, 0);
        MiningLevel.miningLevels.add(only);
        assertEquals(only, only.getBefore());
    }

    // --- skills are independent per level ---

    @Test
    void allSkillProperties_independentPerLevel() {
        MiningLevel level0 = MiningLevel.get(0);
        MiningLevel level1 = MiningLevel.get(1);
        assertNotNull(level0);
        assertNotNull(level1);

        level0.setHasteLevel(1);
        level0.setInstantBreakProbability(10);
        level0.setExtraOreProbability(5);
        level0.setMaxExtraOre(2);

        level1.setHasteLevel(3);
        level1.setInstantBreakProbability(25);
        level1.setExtraOreProbability(15);
        level1.setMaxExtraOre(5);

        assertEquals(1, level0.getHasteLevel());
        assertEquals(10, level0.getInstantBreakProbability());
        assertEquals(5, level0.getExtraOreProbability());
        assertEquals(2, level0.getMaxExtraOre());

        assertEquals(3, level1.getHasteLevel());
        assertEquals(25, level1.getInstantBreakProbability());
        assertEquals(15, level1.getExtraOreProbability());
        assertEquals(5, level1.getMaxExtraOre());
    }

    @Test
    void rewards_independentPerLevel() {
        MiningLevel level0 = MiningLevel.get(0);
        MiningLevel level1 = MiningLevel.get(1);
        assertNotNull(level0);
        assertNotNull(level1);

        level0.addReward(new ItemStack(Material.DIAMOND, 1));
        level1.addReward(new ItemStack(Material.EMERALD, 5));
        level1.addReward(new ItemStack(Material.GOLD_INGOT, 3));

        assertEquals(1, level0.getRewards().length);
        assertEquals(2, level1.getRewards().length);
    }
}
