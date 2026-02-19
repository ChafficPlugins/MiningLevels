package de.chafficplugins.mininglevels.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigStringsTest {

    @Test
    void crucialLibVersion_shouldBe300() {
        assertEquals("3.0.0", ConfigStrings.CRUCIAL_LIB_VERSION);
    }

    @Test
    void permissions_shouldNotBeNull() {
        assertNotNull(ConfigStrings.PERMISSION_ADMIN);
        assertNotNull(ConfigStrings.PERMISSION_SET_LEVEL);
        assertNotNull(ConfigStrings.PERMISSION_SET_XP);
        assertNotNull(ConfigStrings.PERMISSION_LEVEL);
        assertNotNull(ConfigStrings.PERMISSION_RELOAD);
        assertNotNull(ConfigStrings.PERMISSION_EDITOR);
        assertNotNull(ConfigStrings.PERMISSIONS_LEADERBOARD);
        assertNotNull(ConfigStrings.PERMISSION_DEBUG);
    }

    @Test
    void configKeys_shouldNotBeNull() {
        assertNotNull(ConfigStrings.LVL_UP_SOUND);
        assertNotNull(ConfigStrings.MAX_LEVEL_XP_DROPS);
        assertNotNull(ConfigStrings.LEVEL_WITH_PLAYER_PLACED_BLOCKS);
        assertNotNull(ConfigStrings.LEVEL_WITH_GENERATED_BLOCKS);
        assertNotNull(ConfigStrings.LEVEL_PROGRESSION_MESSAGES);
        assertNotNull(ConfigStrings.DESTROY_MINING_BLOCKS_ON_EXPLODE);
        assertNotNull(ConfigStrings.MINING_ITEMS);
        assertNotNull(ConfigStrings.ADMIN_DEBUG);
    }

    @Test
    void messageKeys_shouldNotBeNull() {
        assertNotNull(ConfigStrings.NO_PERMISSION);
        assertNotNull(ConfigStrings.NEW_LEVEL);
        assertNotNull(ConfigStrings.PLAYER_NOT_EXIST);
        assertNotNull(ConfigStrings.XP_RECEIVED);
        assertNotNull(ConfigStrings.LEVEL_OF);
        assertNotNull(ConfigStrings.LEVEL_UNLOCKED);
        assertNotNull(ConfigStrings.LEVEL_NEEDED);
        assertNotNull(ConfigStrings.LEVEL_DROPPED);
        assertNotNull(ConfigStrings.XP_GAINED);
        assertNotNull(ConfigStrings.RELOAD_SUCCESSFUL);
        assertNotNull(ConfigStrings.ERROR_OCCURRED);
    }

    @Test
    void prefix_shouldHaveDefaultValue() {
        assertNotNull(ConfigStrings.PREFIX);
        assertTrue(ConfigStrings.PREFIX.contains("ML"));
    }

    @Test
    void localizedIdentifier_shouldBeMininglevels() {
        assertEquals("mininglevels", ConfigStrings.LOCALIZED_IDENTIFIER);
    }

    @Test
    void permissionAdmin_shouldBeWildcard() {
        assertEquals("mininglevels.*", ConfigStrings.PERMISSION_ADMIN);
    }
}
