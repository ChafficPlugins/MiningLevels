# CLAUDE.md — MiningLevels

## Project Overview

MiningLevels is a Spigot/Paper plugin that adds a configurable mining leveling system to Minecraft servers. Players earn XP by mining blocks, level up, and unlock skills (haste, instant break, extra ore drops) and rewards.

## Build & Test

```bash
mvn clean verify        # compile + test + package
mvn test                # run tests only
mvn package -DskipTests # package without tests
```

- **Java**: 21
- **Server API**: Paper 1.21
- **Build tool**: Maven 3.x
- **Test framework**: JUnit 5 + MockBukkit 4.x

The shaded JAR is produced at `target/MiningLevels-<version>.jar`.

## Key Packages

| Package | Purpose |
|---|---|
| `api` | Core data model: `MiningLevel`, `MiningBlock`, `MiningPlayer`, `Reward` |
| `listeners` | Command handlers and event listeners |
| `listeners/events` | Bukkit event listeners (mining, block placement, server events) |
| `gui` | Inventory-based editor GUIs (levels, blocks, leaderboard) |
| `io` | File I/O (`FileManager`, `MessagesYaml`) |
| `utils` | Config constants, math utilities, messaging helpers |
| `placeholders` | PlaceholderAPI integration |

## Level System

- Levels are defined in `miningLevels.json` (loaded via `MiningLevel.init()`)
- Blocks and their XP values are in `miningBlocks.json` (loaded via `MiningBlock.init()`)
- Player progress is in `players.json` (loaded via `MiningPlayer.init()`)
- All persistence uses CrucialLib's `Json.fromJson()` / `FileManager.saveFile()`

## Event Flow

1. Player breaks a block → `MiningEvents.onBlockBreak()`
2. Block matched against `MiningBlock.getMiningBlock(material)`
3. XP awarded via `MiningPlayer.alterXp(xp)`
4. `xpChange()` checks threshold → calls `MiningLevel.levelUp()` if needed
5. Level-up triggers: sound, message, skill display, rewards, commands

## External Dependencies

- **CrucialLib 3.0.0** — JSON persistence, localization, boss bars, player effects
- **PlaceholderAPI** (optional) — exposes level/XP placeholders and command rewards

## Testing Notes

- Tests use `MockBukkit.mock()` (server only, no plugin load) for domain model tests
- Plugin load is avoided in tests because `Crucial.init()` requires CrucialLib at runtime
- Lazy initialization (`getPlugin()`) ensures classes can be loaded without the plugin being active
- The `MiningLevels` class must NOT be `final` (MockBukkit requirement)

## Config Keys

See `ConfigStrings.java` for all config paths and permission strings. Main config is in `config.yml`, messages in `messages.yml`.
