package de.chafficplugins.mininglevels.api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MiningPlayerTest {

    private static ServerMock server;
    private UUID playerUUID;

    @BeforeAll
    static void setUpServer() {
        server = MockBukkit.mock();
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    @BeforeEach
    void setUp() {
        MiningPlayer.miningPlayers.clear();
        MiningLevel.miningLevels.clear();
        MiningLevel.miningLevels.add(new MiningLevel("Beginner", 100, 0));
        MiningLevel.miningLevels.add(new MiningLevel("Apprentice", 300, 1));
        MiningLevel.miningLevels.add(new MiningLevel("Expert", 500, 2));
        playerUUID = UUID.randomUUID();
    }

    // --- constructor ---

    @Test
    void constructor_shouldCreatePlayer() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        assertNotNull(player);
        assertEquals(playerUUID, player.getUUID());
        assertEquals(0, player.getXp());
        assertNotNull(player.getLevel());
        assertEquals("Beginner", player.getLevel().getName());
    }

    @Test
    void constructor_shouldAddToStaticList() {
        new MiningPlayer(playerUUID, 0, 0);
        assertEquals(1, MiningPlayer.miningPlayers.size());
    }

    @Test
    void constructor_duplicatePlayer_shouldThrow() {
        new MiningPlayer(playerUUID, 0, 0);
        assertThrows(IllegalArgumentException.class, () ->
                new MiningPlayer(playerUUID, 0, 0));
    }

    @Test
    void constructor_withXp_shouldStoreXp() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 75);
        assertEquals(75, player.getXp());
    }

    @Test
    void constructor_withHigherLevel_shouldStoreLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 2, 0);
        assertEquals("Expert", player.getLevel().getName());
    }

    @Test
    void constructor_withLevelAndXp_shouldStoreBoth() {
        MiningPlayer player = new MiningPlayer(playerUUID, 1, 150);
        assertEquals("Apprentice", player.getLevel().getName());
        assertEquals(150, player.getXp());
    }

    // --- getMiningPlayer ---

    @Test
    void getMiningPlayer_existingPlayer_shouldReturn() {
        new MiningPlayer(playerUUID, 0, 0);
        MiningPlayer found = MiningPlayer.getMiningPlayer(playerUUID);
        assertNotNull(found);
        assertEquals(playerUUID, found.getUUID());
    }

    @Test
    void getMiningPlayer_nonExistingPlayer_shouldReturnNull() {
        assertNull(MiningPlayer.getMiningPlayer(UUID.randomUUID()));
    }

    @Test
    void getMiningPlayer_afterMultipleCreations_shouldFindEach() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        new MiningPlayer(uuid1, 0, 10);
        new MiningPlayer(uuid2, 1, 20);
        new MiningPlayer(uuid3, 2, 30);

        assertEquals(10, MiningPlayer.getMiningPlayer(uuid1).getXp());
        assertEquals(20, MiningPlayer.getMiningPlayer(uuid2).getXp());
        assertEquals(30, MiningPlayer.getMiningPlayer(uuid3).getXp());
    }

    @Test
    void getMiningPlayer_emptyList_shouldReturnNull() {
        assertNull(MiningPlayer.getMiningPlayer(UUID.randomUUID()));
    }

    // --- notExists ---

    @Test
    void notExists_newPlayer_shouldReturnTrue() {
        assertTrue(MiningPlayer.notExists(UUID.randomUUID()));
    }

    @Test
    void notExists_existingPlayer_shouldReturnFalse() {
        new MiningPlayer(playerUUID, 0, 0);
        assertFalse(MiningPlayer.notExists(playerUUID));
    }

    @Test
    void notExists_afterCreation_shouldReturnFalse() {
        assertTrue(MiningPlayer.notExists(playerUUID));
        new MiningPlayer(playerUUID, 0, 0);
        assertFalse(MiningPlayer.notExists(playerUUID));
    }

    // --- setXp ---

    @Test
    void setXp_shouldSetWithoutLevelCheck() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setXp(50);
        assertEquals(50, player.getXp());
    }

    @Test
    void setXp_canSetAboveThreshold() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setXp(999);
        assertEquals(999, player.getXp());
        // Level should NOT change since setXp doesn't trigger level check
        assertEquals("Beginner", player.getLevel().getName());
    }

    @Test
    void setXp_zeroXp_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 50);
        player.setXp(0);
        assertEquals(0, player.getXp());
    }

    @Test
    void setXp_negativeXp_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 50);
        player.setXp(-10);
        assertEquals(-10, player.getXp());
    }

    @Test
    void setXp_largeValue_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setXp(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, player.getXp());
    }

    @Test
    void setXp_multipleTimes_shouldOverwrite() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setXp(10);
        assertEquals(10, player.getXp());
        player.setXp(50);
        assertEquals(50, player.getXp());
        player.setXp(0);
        assertEquals(0, player.getXp());
    }

    // --- setLevel ---

    @Test
    void setLevel_byOrdinal_shouldUpdateLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setLevel(1);
        assertEquals("Apprentice", player.getLevel().getName());
    }

    @Test
    void setLevel_byMiningLevel_shouldUpdateLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        MiningLevel expert = MiningLevel.get(2);
        assertNotNull(expert);
        player.setLevel(expert);
        assertEquals("Expert", player.getLevel().getName());
    }

    @Test
    void setLevel_toZero_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 2, 0);
        player.setLevel(0);
        assertEquals("Beginner", player.getLevel().getName());
    }

    @Test
    void setLevel_toMaxLevel_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setLevel(2);
        assertEquals("Expert", player.getLevel().getName());
    }

    @Test
    void setLevel_multipleTimes_shouldUpdateEachTime() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setLevel(1);
        assertEquals("Apprentice", player.getLevel().getName());
        player.setLevel(2);
        assertEquals("Expert", player.getLevel().getName());
        player.setLevel(0);
        assertEquals("Beginner", player.getLevel().getName());
    }

    @Test
    void setLevel_doesNotAffectXp() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 50);
        player.setLevel(2);
        assertEquals(50, player.getXp());
    }

    // --- getLevel ---

    @Test
    void getLevel_shouldReturnCorrectMiningLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 1, 50);
        MiningLevel level = player.getLevel();
        assertNotNull(level);
        assertEquals("Apprentice", level.getName());
        assertEquals(1, level.getOrdinal());
    }

    @Test
    void getLevel_afterSetLevel_shouldReflectChange() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        assertEquals("Beginner", player.getLevel().getName());
        player.setLevel(2);
        assertEquals("Expert", player.getLevel().getName());
    }

    // --- getPlayer / getOfflinePlayer ---

    @Test
    void getPlayer_onlinePlayer_shouldReturnPlayer() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        assertNotNull(mp.getPlayer());
        assertEquals(mockPlayer.getUniqueId(), mp.getPlayer().getUniqueId());
    }

    @Test
    void getOfflinePlayer_shouldReturnOfflinePlayer() {
        MiningPlayer mp = new MiningPlayer(playerUUID, 0, 0);
        assertNotNull(mp.getOfflinePlayer());
        assertEquals(playerUUID, mp.getOfflinePlayer().getUniqueId());
    }

    // --- addRewards ---

    @Test
    void addRewards_singleReward_shouldBeStored() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        mp.addRewards(new ItemStack(Material.DIAMOND, 5));
        // claim returns 1 if rewards were claimed
        int result = mp.claim();
        assertEquals(1, result);
    }

    @Test
    void addRewards_multipleRewards_shouldAllBeStored() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        mp.addRewards(
                new ItemStack(Material.DIAMOND, 5),
                new ItemStack(Material.IRON_INGOT, 10),
                new ItemStack(Material.GOLD_INGOT, 3)
        );
        int result = mp.claim();
        assertEquals(1, result);
    }

    // --- claim ---

    @Test
    void claim_noRewards_shouldReturnZero() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        assertEquals(0, mp.claim());
    }

    @Test
    void claim_withRewards_shouldReturnOne() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        mp.addRewards(new ItemStack(Material.DIAMOND, 1));
        assertEquals(1, mp.claim());
    }

    @Test
    void claim_afterClaiming_shouldReturnZero() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);
        mp.addRewards(new ItemStack(Material.DIAMOND, 1));
        assertEquals(1, mp.claim());
        // no more rewards
        assertEquals(0, mp.claim());
    }

    // --- equals ---

    @Test
    void equals_sameUUID_shouldBeEqual() {
        MiningPlayer player1 = new MiningPlayer(playerUUID, 0, 0);
        assertEquals(player1, player1);
    }

    @Test
    void equals_differentUUID_shouldNotBeEqual() {
        MiningPlayer player1 = new MiningPlayer(playerUUID, 0, 0);
        MiningPlayer player2 = new MiningPlayer(UUID.randomUUID(), 0, 0);
        assertNotEquals(player1, player2);
    }

    @Test
    void equals_nonMiningPlayerObject_shouldReturnFalse() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        assertNotEquals(player, "not a player");
    }

    @Test
    void equals_null_shouldReturnFalse() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        assertNotEquals(player, null);
    }

    @Test
    void equals_sameUUIDDifferentLevelAndXp_shouldBeEqual() {
        // equals is UUID-based, so same UUID = equal regardless of level/xp
        // But we can't create two with the same UUID, so test via self-equality
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setLevel(2);
        player.setXp(999);
        assertEquals(player, player);
    }

    // --- multiplePlayers ---

    @Test
    void multiplePlayers_shouldAllBeTracked() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        new MiningPlayer(uuid1, 0, 0);
        new MiningPlayer(uuid2, 1, 50);
        new MiningPlayer(uuid3, 2, 200);

        assertEquals(3, MiningPlayer.miningPlayers.size());
        assertNotNull(MiningPlayer.getMiningPlayer(uuid1));
        assertNotNull(MiningPlayer.getMiningPlayer(uuid2));
        assertNotNull(MiningPlayer.getMiningPlayer(uuid3));
    }

    @Test
    void multiplePlayers_eachHasCorrectState() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        new MiningPlayer(uuid1, 0, 10);
        new MiningPlayer(uuid2, 2, 400);

        MiningPlayer p1 = MiningPlayer.getMiningPlayer(uuid1);
        MiningPlayer p2 = MiningPlayer.getMiningPlayer(uuid2);

        assertNotNull(p1);
        assertNotNull(p2);
        assertEquals("Beginner", p1.getLevel().getName());
        assertEquals(10, p1.getXp());
        assertEquals("Expert", p2.getLevel().getName());
        assertEquals(400, p2.getXp());
    }

    // --- level boundary scenarios ---

    @Test
    void playerWithLevel0_shouldHaveBeginnerLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        assertEquals("Beginner", player.getLevel().getName());
        assertEquals(0, player.getLevel().getOrdinal());
    }

    @Test
    void playerWithMaxLevel_shouldHaveMaxLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 2, 0);
        assertEquals("Expert", player.getLevel().getName());
    }

    // --- XP and level combinations ---

    @Test
    void setXp_doesNotChangeLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        player.setXp(9999);
        assertEquals("Beginner", player.getLevel().getName());
    }

    @Test
    void setLevel_doesNotChangeXp() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 75);
        player.setLevel(2);
        assertEquals(75, player.getXp());
        assertEquals("Expert", player.getLevel().getName());
    }

    @Test
    void player_setAndGetUUID_shouldBeConsistent() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 0);
        UUID retrieved = player.getUUID();
        assertEquals(playerUUID, retrieved);
        assertSame(playerUUID, retrieved);
    }

    // --- many players stress ---

    @Test
    void manyPlayers_shouldAllBeTrackable() {
        for (int i = 0; i < 100; i++) {
            new MiningPlayer(UUID.randomUUID(), i % 3, i);
        }
        assertEquals(100, MiningPlayer.miningPlayers.size());
    }

    @Test
    void manyPlayers_lookupShouldFindAll() {
        UUID[] uuids = new UUID[50];
        for (int i = 0; i < 50; i++) {
            uuids[i] = UUID.randomUUID();
            new MiningPlayer(uuids[i], i % 3, i * 10);
        }
        for (int i = 0; i < 50; i++) {
            MiningPlayer found = MiningPlayer.getMiningPlayer(uuids[i]);
            assertNotNull(found);
            assertEquals(i * 10, found.getXp());
        }
    }
}
