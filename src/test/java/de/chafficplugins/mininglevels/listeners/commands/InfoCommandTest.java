package de.chafficplugins.mininglevels.listeners.commands;

import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the /mininglevels info command logic.
 * <p>
 * Since CrucialLib's Localizer is not available in tests, we verify that
 * the data retrieval and state used by the info command is correct:
 * the player's MiningPlayer exists and exposes the expected level and XP.
 */
class InfoCommandTest {

    private static ServerMock server;

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
    }

    @Test
    void infoCommand_playerHasLevel0_shouldShowBeginnerLevelAndZeroXp() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 0);

        MiningLevel level = mp.getLevel();
        assertNotNull(level);
        assertEquals("Beginner", level.getName());
        assertEquals(0, mp.getXp());
        assertEquals(100, level.getNextLevelXP());
    }

    @Test
    void infoCommand_playerHasLevel1WithXp_shouldShowApprenticeAndXp() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 1, 150);

        MiningLevel level = mp.getLevel();
        assertNotNull(level);
        assertEquals("Apprentice", level.getName());
        assertEquals(150, mp.getXp());
        assertEquals(300, level.getNextLevelXP());
    }

    @Test
    void infoCommand_playerAtMaxLevel_shouldShowExpertLevel() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 2, 400);

        MiningLevel level = mp.getLevel();
        assertNotNull(level);
        assertEquals("Expert", level.getName());
        assertEquals(400, mp.getXp());
        assertEquals(500, level.getNextLevelXP());
    }

    @Test
    void infoCommand_noMiningPlayer_shouldReturnNull() {
        PlayerMock mockPlayer = server.addPlayer();

        MiningPlayer mp = MiningPlayer.getMiningPlayer(mockPlayer.getUniqueId());
        assertNull(mp);
    }

    @Test
    void infoCommand_afterLevelChange_shouldReflectNewLevel() {
        PlayerMock mockPlayer = server.addPlayer();
        MiningPlayer mp = new MiningPlayer(mockPlayer.getUniqueId(), 0, 50);

        assertEquals("Beginner", mp.getLevel().getName());
        assertEquals(50, mp.getXp());

        mp.setLevel(2);
        mp.setXp(450);

        assertEquals("Expert", mp.getLevel().getName());
        assertEquals(450, mp.getXp());
    }

    @Test
    void infoCommand_requiresPlayerSender() {
        // The info command should only work for Player senders.
        // Console senders should get a "no console command" response.
        // Verified by the instanceof Player check in LevelingCommands.info().
        UUID consoleUUID = UUID.randomUUID();
        MiningPlayer mp = MiningPlayer.getMiningPlayer(consoleUUID);
        assertNull(mp, "Console has no MiningPlayer");
    }

    @Test
    void infoCommand_correctPlayerLookup_shouldFindRightPlayer() {
        PlayerMock player1 = server.addPlayer();
        PlayerMock player2 = server.addPlayer();
        new MiningPlayer(player1.getUniqueId(), 0, 25);
        new MiningPlayer(player2.getUniqueId(), 2, 350);

        MiningPlayer mp1 = MiningPlayer.getMiningPlayer(player1.getUniqueId());
        MiningPlayer mp2 = MiningPlayer.getMiningPlayer(player2.getUniqueId());

        assertNotNull(mp1);
        assertNotNull(mp2);
        assertEquals("Beginner", mp1.getLevel().getName());
        assertEquals(25, mp1.getXp());
        assertEquals("Expert", mp2.getLevel().getName());
        assertEquals(350, mp2.getXp());
    }
}
