package de.chafficplugins.mininglevels.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MiningPlayerTest {

    private UUID playerUUID;

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
        MiningPlayer.miningPlayers.clear();
        MiningLevel.miningLevels.clear();
        MiningLevel.miningLevels.add(new MiningLevel("Beginner", 100, 0));
        MiningLevel.miningLevels.add(new MiningLevel("Apprentice", 300, 1));
        MiningLevel.miningLevels.add(new MiningLevel("Expert", 500, 2));
        playerUUID = UUID.randomUUID();
    }

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
    void notExists_newPlayer_shouldReturnTrue() {
        assertTrue(MiningPlayer.notExists(UUID.randomUUID()));
    }

    @Test
    void notExists_existingPlayer_shouldReturnFalse() {
        new MiningPlayer(playerUUID, 0, 0);
        assertFalse(MiningPlayer.notExists(playerUUID));
    }

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
    }

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
    void getLevel_shouldReturnCorrectMiningLevel() {
        MiningPlayer player = new MiningPlayer(playerUUID, 1, 50);
        MiningLevel level = player.getLevel();
        assertNotNull(level);
        assertEquals("Apprentice", level.getName());
        assertEquals(1, level.getOrdinal());
    }

    @Test
    void equals_sameUUID_shouldBeEqual() {
        MiningPlayer player1 = new MiningPlayer(playerUUID, 0, 0);
        // Can't create another with same UUID (throws), so test self-equality
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

    @Test
    void setXp_zeroXp_shouldWork() {
        MiningPlayer player = new MiningPlayer(playerUUID, 0, 50);
        player.setXp(0);
        assertEquals(0, player.getXp());
    }
}
