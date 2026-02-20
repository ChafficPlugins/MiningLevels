package de.chafficplugins.mininglevels.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validates the bundled default configuration files (levels.json and blocks.json)
 * to catch issues like invalid values, missing fields, or broken cross-references.
 * <p>
 * These tests load the JSON directly from the classpath without CrucialLib,
 * ensuring the defaults ship in a valid, consistent state.
 */
class DefaultConfigTest {

    private static JsonArray levels;
    private static JsonArray blocks;

    @BeforeAll
    static void setUp() {
        MockBukkit.mock();
        levels = loadJsonArray("/defaults/levels.json");
        blocks = loadJsonArray("/defaults/blocks.json");
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    private static JsonArray loadJsonArray(String path) {
        try (InputStream is = DefaultConfigTest.class.getResourceAsStream(path)) {
            assertNotNull(is, "Resource not found: " + path);
            try (Reader reader = new InputStreamReader(is)) {
                JsonElement element = JsonParser.parseReader(reader);
                assertTrue(element.isJsonArray(), path + " should be a JSON array");
                return element.getAsJsonArray();
            }
        } catch (IOException e) {
            return fail("Failed to read " + path + ": " + e.getMessage());
        }
    }

    // ---- levels.json ----

    @Test
    void levelsJson_shouldNotBeEmpty() {
        assertFalse(levels.isEmpty(), "levels.json should contain at least one level");
    }

    @Test
    void levelsJson_shouldHaveRequiredFields() {
        for (JsonElement el : levels) {
            JsonObject level = el.getAsJsonObject();
            assertTrue(level.has("name"), "Level missing 'name' field");
            assertTrue(level.has("nextLevelXP"), "Level missing 'nextLevelXP' field");
            assertTrue(level.has("ordinal"), "Level missing 'ordinal' field");
        }
    }

    @Test
    void levelsJson_ordinalsShouldBeSequential() {
        for (int i = 0; i < levels.size(); i++) {
            int ordinal = levels.get(i).getAsJsonObject().get("ordinal").getAsInt();
            assertEquals(i, ordinal, "Level at index " + i + " should have ordinal " + i);
        }
    }

    @Test
    void levelsJson_xpThresholdsShouldBePositive() {
        for (JsonElement el : levels) {
            JsonObject level = el.getAsJsonObject();
            int xp = level.get("nextLevelXP").getAsInt();
            assertTrue(xp > 0, "Level '" + level.get("name").getAsString()
                    + "' should have positive XP threshold, got " + xp);
        }
    }

    @Test
    void levelsJson_xpThresholdsShouldIncrease() {
        int previousXp = 0;
        for (JsonElement el : levels) {
            JsonObject level = el.getAsJsonObject();
            int xp = level.get("nextLevelXP").getAsInt();
            assertTrue(xp > previousXp, "Level '" + level.get("name").getAsString()
                    + "' XP (" + xp + ") should be greater than previous (" + previousXp + ")");
            previousXp = xp;
        }
    }

    @Test
    void levelsJson_probabilitiesShouldBeInRange() {
        for (JsonElement el : levels) {
            JsonObject level = el.getAsJsonObject();
            String name = level.get("name").getAsString();
            if (level.has("instantBreakProbability")) {
                float p = level.get("instantBreakProbability").getAsFloat();
                assertTrue(p >= 0 && p <= 100,
                        "Level '" + name + "' instantBreakProbability " + p + " out of range [0,100]");
            }
            if (level.has("extraOreProbability")) {
                float p = level.get("extraOreProbability").getAsFloat();
                assertTrue(p >= 0 && p <= 100,
                        "Level '" + name + "' extraOreProbability " + p + " out of range [0,100]");
            }
        }
    }

    @Test
    void levelsJson_hasteAndExtraOreShouldBeNonNegative() {
        for (JsonElement el : levels) {
            JsonObject level = el.getAsJsonObject();
            String name = level.get("name").getAsString();
            if (level.has("maxExtraOre")) {
                int v = level.get("maxExtraOre").getAsInt();
                assertTrue(v >= 0, "Level '" + name + "' maxExtraOre should be >= 0, got " + v);
            }
            if (level.has("hasteLevel")) {
                int v = level.get("hasteLevel").getAsInt();
                assertTrue(v >= 0, "Level '" + name + "' hasteLevel should be >= 0, got " + v);
            }
        }
    }

    // ---- blocks.json ----

    @Test
    void blocksJson_shouldNotBeEmpty() {
        assertFalse(blocks.isEmpty(), "blocks.json should contain at least one block");
    }

    @Test
    void blocksJson_shouldHaveRequiredFields() {
        for (int i = 0; i < blocks.size(); i++) {
            JsonObject block = blocks.get(i).getAsJsonObject();
            assertTrue(block.has("materials"), "Block " + i + " missing 'materials' field");
            assertTrue(block.has("xp"), "Block " + i + " missing 'xp' field");
            assertTrue(block.has("minLevel"), "Block " + i + " missing 'minLevel' field");
        }
    }

    @Test
    void blocksJson_materialsShouldBeValidBukkitMaterials() {
        for (int i = 0; i < blocks.size(); i++) {
            JsonObject block = blocks.get(i).getAsJsonObject();
            JsonArray materials = block.getAsJsonArray("materials");
            assertFalse(materials.isEmpty(), "Block " + i + " should have at least one material");
            for (JsonElement mat : materials) {
                String name = mat.getAsString();
                assertDoesNotThrow(() -> Material.valueOf(name),
                        "Block " + i + " has invalid material: " + name);
                assertTrue(Material.valueOf(name).isBlock(),
                        "Material " + name + " in block " + i + " should be a block type");
            }
        }
    }

    @Test
    void blocksJson_xpShouldBePositive() {
        for (int i = 0; i < blocks.size(); i++) {
            JsonObject block = blocks.get(i).getAsJsonObject();
            int xp = block.get("xp").getAsInt();
            assertTrue(xp > 0, "Block " + i + " should have positive XP, got " + xp);
        }
    }

    @Test
    void blocksJson_minLevelsShouldReferenceExistingLevels() {
        int maxOrdinal = levels.size() - 1;
        for (int i = 0; i < blocks.size(); i++) {
            JsonObject block = blocks.get(i).getAsJsonObject();
            int minLevel = block.get("minLevel").getAsInt();
            assertTrue(minLevel >= 0 && minLevel <= maxOrdinal,
                    "Block " + i + " minLevel " + minLevel + " out of range [0," + maxOrdinal + "]");
        }
    }

    @Test
    void blocksJson_shouldNotHaveDuplicateMaterials() {
        Set<String> allMaterials = new HashSet<>();
        for (int i = 0; i < blocks.size(); i++) {
            JsonArray materials = blocks.get(i).getAsJsonObject().getAsJsonArray("materials");
            for (JsonElement mat : materials) {
                String name = mat.getAsString();
                assertTrue(allMaterials.add(name),
                        "Duplicate material '" + name + "' found in block " + i);
            }
        }
    }
}
