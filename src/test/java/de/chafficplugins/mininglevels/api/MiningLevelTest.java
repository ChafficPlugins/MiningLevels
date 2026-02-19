package de.chafficplugins.mininglevels.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

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

    @Test
    void get_shouldReturnCorrectLevel() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals("Beginner", level.getName());
        assertEquals(0, level.getOrdinal());
    }

    @Test
    void get_shouldReturnNullForInvalidOrdinal() {
        assertNull(MiningLevel.get(99));
        assertNull(MiningLevel.get(-1));
    }

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
    void getNextLevelXP_shouldReturnCorrectValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        assertEquals(100, level.getNextLevelXP());
    }

    @Test
    void setNextLevelXP_shouldUpdateValue() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setNextLevelXP(200);
        assertEquals(200, level.getNextLevelXP());
    }

    @Test
    void getMaxLevel_shouldReturnLastLevel() {
        MiningLevel maxLevel = MiningLevel.getMaxLevel();
        assertNotNull(maxLevel);
        assertEquals("Master", maxLevel.getName());
        assertEquals(3, maxLevel.getOrdinal());
    }

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
    void setMaxExtraOre_negative_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setMaxExtraOre(5);
        level.setMaxExtraOre(-1);
        assertEquals(5, level.getMaxExtraOre());
    }

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
    void setHasteLevel_negative_shouldNotChange() {
        MiningLevel level = MiningLevel.get(0);
        assertNotNull(level);
        level.setHasteLevel(3);
        level.setHasteLevel(-1);
        assertEquals(3, level.getHasteLevel());
    }

    @Test
    void levelListSize_shouldBeFour() {
        assertEquals(4, MiningLevel.miningLevels.size());
    }
}
