package de.chafficplugins.mininglevels.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigStringsTest {

    // --- version ---

    @Test
    void crucialLibVersion_shouldBe300() {
        assertEquals("3.0.0", ConfigStrings.CRUCIAL_LIB_VERSION);
    }

    // --- identifiers ---

    @Test
    void localizedIdentifier_shouldBeMininglevels() {
        assertEquals("mininglevels", ConfigStrings.LOCALIZED_IDENTIFIER);
    }

    @Test
    void spigotId_shouldBePositive() {
        assertTrue(ConfigStrings.SPIGOT_ID > 0);
        assertEquals(100886, ConfigStrings.SPIGOT_ID);
    }

    @Test
    void bstatsId_shouldBePositive() {
        assertTrue(ConfigStrings.BSTATS_ID > 0);
        assertEquals(14709, ConfigStrings.BSTATS_ID);
    }

    // --- prefix ---

    @Test
    void prefix_shouldHaveDefaultValue() {
        assertNotNull(ConfigStrings.PREFIX);
        assertTrue(ConfigStrings.PREFIX.contains("ML"));
    }

    @Test
    void prefix_shouldContainBrackets() {
        assertTrue(ConfigStrings.PREFIX.contains("["));
        assertTrue(ConfigStrings.PREFIX.contains("]"));
    }

    // --- permissions ---

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
    void permissionAdmin_shouldBeWildcard() {
        assertEquals("mininglevels.*", ConfigStrings.PERMISSION_ADMIN);
    }

    @Test
    void permissions_shouldStartWithMininglevels() {
        assertTrue(ConfigStrings.PERMISSION_ADMIN.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_SET_LEVEL.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_SET_XP.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_LEVEL.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_RELOAD.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_EDITOR.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSIONS_LEADERBOARD.startsWith("mininglevels."));
        assertTrue(ConfigStrings.PERMISSION_DEBUG.startsWith("mininglevels."));
    }

    @Test
    void permissions_shouldHaveCorrectValues() {
        assertEquals("mininglevels.setlevel", ConfigStrings.PERMISSION_SET_LEVEL);
        assertEquals("mininglevels.setxp", ConfigStrings.PERMISSION_SET_XP);
        assertEquals("mininglevels.level", ConfigStrings.PERMISSION_LEVEL);
        assertEquals("mininglevels.reload", ConfigStrings.PERMISSION_RELOAD);
        assertEquals("mininglevels.editor", ConfigStrings.PERMISSION_EDITOR);
        assertEquals("mininglevels.leaderboard", ConfigStrings.PERMISSIONS_LEADERBOARD);
        assertEquals("mininglevels.debug", ConfigStrings.PERMISSION_DEBUG);
    }

    @Test
    void permissions_shouldAllBeDistinct() {
        String[] perms = {
                ConfigStrings.PERMISSION_ADMIN,
                ConfigStrings.PERMISSION_SET_LEVEL,
                ConfigStrings.PERMISSION_SET_XP,
                ConfigStrings.PERMISSION_LEVEL,
                ConfigStrings.PERMISSION_RELOAD,
                ConfigStrings.PERMISSION_EDITOR,
                ConfigStrings.PERMISSIONS_LEADERBOARD,
                ConfigStrings.PERMISSION_DEBUG
        };
        for (int i = 0; i < perms.length; i++) {
            for (int j = i + 1; j < perms.length; j++) {
                assertNotEquals(perms[i], perms[j],
                        "Duplicate permission: " + perms[i]);
            }
        }
    }

    // --- config keys ---

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
    void configKeys_shouldHaveCorrectValues() {
        assertEquals("levelup_sound", ConfigStrings.LVL_UP_SOUND);
        assertEquals("max_level_xp_drops", ConfigStrings.MAX_LEVEL_XP_DROPS);
        assertEquals("level_with.player_placed_blocks", ConfigStrings.LEVEL_WITH_PLAYER_PLACED_BLOCKS);
        assertEquals("level_with.generated_blocks", ConfigStrings.LEVEL_WITH_GENERATED_BLOCKS);
        assertEquals("level_progression_messages", ConfigStrings.LEVEL_PROGRESSION_MESSAGES);
        assertEquals("destroy_mining_blocks_on_explode", ConfigStrings.DESTROY_MINING_BLOCKS_ON_EXPLODE);
        assertEquals("mining_items", ConfigStrings.MINING_ITEMS);
        assertEquals("admin.debug", ConfigStrings.ADMIN_DEBUG);
    }

    @Test
    void configKeys_shouldNotBeEmpty() {
        assertFalse(ConfigStrings.LVL_UP_SOUND.isEmpty());
        assertFalse(ConfigStrings.MAX_LEVEL_XP_DROPS.isEmpty());
        assertFalse(ConfigStrings.LEVEL_WITH_PLAYER_PLACED_BLOCKS.isEmpty());
        assertFalse(ConfigStrings.LEVEL_WITH_GENERATED_BLOCKS.isEmpty());
        assertFalse(ConfigStrings.LEVEL_PROGRESSION_MESSAGES.isEmpty());
        assertFalse(ConfigStrings.DESTROY_MINING_BLOCKS_ON_EXPLODE.isEmpty());
        assertFalse(ConfigStrings.MINING_ITEMS.isEmpty());
        assertFalse(ConfigStrings.ADMIN_DEBUG.isEmpty());
    }

    // --- message keys ---

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
    void messageKeys_shouldNotBeEmpty() {
        assertFalse(ConfigStrings.NO_PERMISSION.isEmpty());
        assertFalse(ConfigStrings.NEW_LEVEL.isEmpty());
        assertFalse(ConfigStrings.PLAYER_NOT_EXIST.isEmpty());
        assertFalse(ConfigStrings.XP_RECEIVED.isEmpty());
        assertFalse(ConfigStrings.LEVEL_OF.isEmpty());
        assertFalse(ConfigStrings.LEVEL_UNLOCKED.isEmpty());
        assertFalse(ConfigStrings.LEVEL_NEEDED.isEmpty());
        assertFalse(ConfigStrings.LEVEL_DROPPED.isEmpty());
        assertFalse(ConfigStrings.XP_GAINED.isEmpty());
        assertFalse(ConfigStrings.RELOAD_SUCCESSFUL.isEmpty());
        assertFalse(ConfigStrings.ERROR_OCCURRED.isEmpty());
    }

    @Test
    void skillChangeMessages_shouldNotBeNull() {
        assertNotNull(ConfigStrings.HASTELVL_CHANGE);
        assertNotNull(ConfigStrings.INSTANT_BREAK_CHANGE);
        assertNotNull(ConfigStrings.EXTRA_ORE_CHANGE);
        assertNotNull(ConfigStrings.MAX_EXTRA_ORE_CHANGE);
    }

    @Test
    void rewardMessages_shouldNotBeNull() {
        assertNotNull(ConfigStrings.REWARDS_LIST);
        assertNotNull(ConfigStrings.CLAIM_YOUR_REWARD);
        assertNotNull(ConfigStrings.NO_REWARDS);
        assertNotNull(ConfigStrings.REWARDS_CLAIMED);
        assertNotNull(ConfigStrings.NO_MORE_SPACE);
    }

    @Test
    void uiMessages_shouldNotBeNull() {
        assertNotNull(ConfigStrings.CURRENT_LEVEL);
        assertNotNull(ConfigStrings.CURRENT_XP);
        assertNotNull(ConfigStrings.CURRENT_HASTE_LEVEL);
        assertNotNull(ConfigStrings.CURRENT_INSTANT_BREAK_LEVEL);
        assertNotNull(ConfigStrings.CURRENT_EXTRA_ORE_LEVEL);
        assertNotNull(ConfigStrings.CURRENT_MAX_EXTRA_ORE);
    }

    @Test
    void usageMessages_shouldNotBeNull() {
        assertNotNull(ConfigStrings.USAGE_SET_LEVEL);
        assertNotNull(ConfigStrings.USAGE_SET_XP);
        assertNotNull(ConfigStrings.USAGE_LEVEL);
    }

    @Test
    void miscMessages_shouldNotBeNull() {
        assertNotNull(ConfigStrings.NO_CONSOLE_COMMAND);
        assertNotNull(ConfigStrings.ONLY_BLOCKS_ALLOWED);
        assertNotNull(ConfigStrings.CANT_DELETE_LEVEL);
        assertNotNull(ConfigStrings.LEADERBOARD_HEADER);
        assertNotNull(ConfigStrings.CLOSE);
    }

    // --- download URLs ---

    @Test
    void downloadUrl_shouldNotBeNull() {
        assertNotNull(ConfigStrings.DOWNLOAD_URL);
        assertFalse(ConfigStrings.DOWNLOAD_URL.isEmpty());
    }

    @Test
    void jsonFileIds_shouldNotBeNull() {
        assertNotNull(ConfigStrings.MINING_LEVELS_JSON);
        assertNotNull(ConfigStrings.MINING_BLOCKS_JSON);
        assertFalse(ConfigStrings.MINING_LEVELS_JSON.isEmpty());
        assertFalse(ConfigStrings.MINING_BLOCKS_JSON.isEmpty());
    }

    @Test
    void jsonFileIds_shouldBeDifferent() {
        assertNotEquals(ConfigStrings.MINING_LEVELS_JSON, ConfigStrings.MINING_BLOCKS_JSON);
    }

    // --- all message keys are distinct ---

    @Test
    void messageKeys_shouldAllBeDistinct() {
        String[] keys = {
                ConfigStrings.NO_PERMISSION, ConfigStrings.NEW_LEVEL,
                ConfigStrings.PLAYER_NOT_EXIST, ConfigStrings.XP_RECEIVED,
                ConfigStrings.LEVEL_OF, ConfigStrings.LEVEL_UNLOCKED,
                ConfigStrings.HASTELVL_CHANGE, ConfigStrings.INSTANT_BREAK_CHANGE,
                ConfigStrings.EXTRA_ORE_CHANGE, ConfigStrings.MAX_EXTRA_ORE_CHANGE,
                ConfigStrings.REWARDS_LIST, ConfigStrings.CLAIM_YOUR_REWARD,
                ConfigStrings.LEVEL_DROPPED, ConfigStrings.XP_GAINED,
                ConfigStrings.RELOAD_SUCCESSFUL, ConfigStrings.ERROR_OCCURRED,
                ConfigStrings.NO_CONSOLE_COMMAND, ConfigStrings.CURRENT_LEVEL,
                ConfigStrings.CURRENT_XP, ConfigStrings.CURRENT_HASTE_LEVEL,
                ConfigStrings.CURRENT_INSTANT_BREAK_LEVEL, ConfigStrings.CURRENT_EXTRA_ORE_LEVEL,
                ConfigStrings.CURRENT_MAX_EXTRA_ORE, ConfigStrings.ONLY_BLOCKS_ALLOWED,
                ConfigStrings.CANT_DELETE_LEVEL, ConfigStrings.NO_REWARDS,
                ConfigStrings.REWARDS_CLAIMED, ConfigStrings.NO_MORE_SPACE,
                ConfigStrings.LEVEL_NEEDED, ConfigStrings.LEADERBOARD_HEADER,
                ConfigStrings.USAGE_SET_LEVEL, ConfigStrings.USAGE_SET_XP,
                ConfigStrings.USAGE_LEVEL, ConfigStrings.CLOSE
        };
        for (int i = 0; i < keys.length; i++) {
            for (int j = i + 1; j < keys.length; j++) {
                assertNotEquals(keys[i], keys[j],
                        "Duplicate message key: " + keys[i]);
            }
        }
    }
}
