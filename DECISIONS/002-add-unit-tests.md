# ADR-002: Add Unit Tests with MockBukkit

## Status
Accepted

## Context
The project had no automated tests. MockBukkit provides a mock Bukkit server for testing Spigot/Paper plugins without a real Minecraft server.

## Decision
- Use **JUnit 5** as the test framework
- Use **MockBukkit 4.101.0** (for Paper 1.21) as the Bukkit mock
- Test domain model classes (`MiningLevel`, `MiningBlock`, `MiningPlayer`, `Reward`) and utilities (`MathUtils`, `ConfigStrings`)
- Use `MockBukkit.mock()` only (no `MockBukkit.load()`) for domain model tests

## Why Not MockBukkit.load()?
The plugin's `onLoad()` calls `Crucial.init()` which attempts to find/download CrucialLib — a runtime dependency not available in the test environment. Since domain model tests only need Bukkit Material/ItemStack classes (not the full plugin lifecycle), `MockBukkit.mock()` provides sufficient infrastructure.

## Design Decisions
- **Lazy plugin initialization**: Static `getPlugin()` methods in `MiningLevel`, `MiningPlayer`, and `Crucial` use lazy init to avoid `JavaPlugin.getPlugin()` calls at class load time (which fails under MockBukkit)
- **MiningLevels class is non-final**: MockBukkit requires subclassing the plugin class
- **Paper API as compile dependency**: MockBukkit 4.x is built against Paper, not Spigot

## Test Coverage
- `MathUtilsTest` — 121 tests (randomDouble range behavior)
- `ConfigStringsTest` — 7 tests (constant validation)
- `MiningLevelTest` — 24 tests (level data model, navigation, skills)
- `MiningBlockTest` — 10 tests (block creation, lookup, validation)
- `MiningPlayerTest` — 19 tests (player lifecycle, XP, levels)
- `RewardTest` — 3 tests (reward creation, item stacks)

**Total: 184 tests**
