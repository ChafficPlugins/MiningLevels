# Architecture — MiningLevels

## High-Level Overview

```
┌─────────────────────────────────────────────────┐
│                  MiningLevels                    │
│               (JavaPlugin entry)                 │
├──────────┬──────────┬───────────┬───────────────┤
│   API    │ Listeners│    GUI    │     Utils      │
│          │          │           │                │
│ MiningLvl│ Commands │ LevelList │ ConfigStrings  │
│ MiningBlk│ Events   │ LevelEdit │ MathUtils      │
│ MiningPly│          │ BlockList │ SenderUtils    │
│ Reward   │          │ BlockEdit │ Crucial        │
│          │          │ Leaderboard│ CustomMessages│
├──────────┴──────────┴───────────┴───────────────┤
│                     I/O                          │
│          FileManager · MessagesYaml              │
├─────────────────────────────────────────────────┤
│              CrucialLib 3.0.0                    │
│         (JSON, localization, effects)            │
└─────────────────────────────────────────────────┘
```

## Package Structure

### `api` — Core Data Model

- **MiningLevel**: Defines a level with name, ordinal, XP threshold, and skills (haste, instant break, extra ore, rewards, commands). Stored as ordered list.
- **MiningBlock**: Maps one or more `Material` names to an XP value and minimum level requirement.
- **MiningPlayer**: Tracks a player's UUID, current level ordinal, XP, and unclaimed rewards. Provides `alterXp()` for XP changes with automatic level-up detection.
- **Reward**: Wraps an `ItemStack` for JSON serialization (material + amount).

All model classes use **static ArrayLists** as in-memory registries (`miningLevels`, `miningBlocks`, `miningPlayers`) loaded from JSON at startup.

### `listeners` — Commands & Events

- **MiningLevelsCommandListener**: Handles `/mininglevels` (aliases: `/ml`). Subcommands: level, setlevel, setxp, reload, editor, leaderboard.
- **RewardCommandListener**: Handles `/miningrewards` for claiming level-up rewards.
- **MiningEvents**: Core gameplay — block break XP, haste effect, instant break, extra ore drops.
- **NoXpBlockEvents**: Tracks player-placed blocks to prevent XP farming.
- **ServerEvents**: Player join/quit handling (creates new MiningPlayers, saves data).

### `gui` — Inventory Editor

In-game GUIs built with CrucialLib's `Interface` system for editing levels and blocks. Admin-only (`mininglevels.editor`).

### `io` — Persistence

- **FileManager**: Manages JSON data files (`miningLevels.json`, `miningBlocks.json`, `players.json`). Uses CrucialLib's `Json` class.
- **MessagesYaml**: Loads `messages.yml` for CrucialLib's `Localizer`.

### `utils` — Helpers

- **ConfigStrings**: All config keys, permissions, message keys, and constants.
- **Crucial**: CrucialLib dependency checker — downloads if missing, validates version.
- **MathUtils**: `randomDouble(min, max)` for probability calculations.
- **SenderUtils**: Chat message formatting with prefix support.
- **CustomMessages**: Loads custom message configuration.

### `placeholders` — PlaceholderAPI

- **LevelPlaceholders**: Registers `%mininglevels_level%`, `%mininglevels_xp%`, etc.
- **PlaceholderCommand**: Executes commands with placeholder replacement on level-up.

## Data Flow

### Startup Sequence

```
onLoad()  → Crucial.init()      (ensure CrucialLib is available)
onEnable() → Crucial.connect()  (enable CrucialLib)
           → loadConfig()       (config.yml defaults)
           → FileManager()      (ensure data directory)
           → MiningLevel.init() (load miningLevels.json)
           → MiningPlayer.init()(load players.json)
           → MiningBlock.init() (load miningBlocks.json)
           → register listeners, commands, placeholders
```

### Mining Event Flow

```
BlockBreakEvent
  → Check mining item in hand (config: mining_items)
  → Check player-placed block tracking
  → MiningBlock.getMiningBlock(material)
  → Check minLevel requirement
  → Apply haste effect (PotionEffectType.HASTE)
  → Apply instant break probability
  → Apply extra ore probability
  → MiningPlayer.alterXp(block.getXp())
    → xpChange()
      → If xp >= threshold: levelUp()
      → If xp < 0: level drop
      → Otherwise: show progress
```

### Persistence

All data is JSON-based via CrucialLib. Saved on:
- Plugin disable (`onDisable()`)
- Editor changes (immediate save)
- Config reload command
