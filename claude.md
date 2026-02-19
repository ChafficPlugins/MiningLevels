# MiningLevels

An easily configurable advanced mining level system for Spigot/Minecraft servers. Players gain XP by mining configured blocks, level up through a progression system, unlock skills (haste, instant break, extra ore drops), and claim item rewards.

**Version:** 1.2.10
**Author:** ChafficPlugins
**Spigot API:** 1.15+ (tested on 1.15–1.19)
**Java:** Compiled to Java 14 bytecode; requires Java 16+ at runtime

## Build System

Maven project. Build with:

```
mvn -B package --file pom.xml
```

The Maven Shade Plugin bundles dependencies into the final JAR. CI uses GitHub Actions (`.github/workflows/maven.yml`) with JDK 18 on `ubuntu-latest`, triggered on push/PR to `master`.

### Dependencies

| Dependency | Version | Scope | Purpose |
|---|---|---|---|
| Spigot API | 1.18-R0.1-SNAPSHOT | provided | Server framework |
| CrucialAPI | 2.1.7 | provided | GUI framework, localization, JSON I/O, utilities |
| PlaceholderAPI | 2.11.5 | provided | Placeholder expansion (optional) |
| JUnit Jupiter | 5.9.0 | test | Unit testing |

Both CrucialAPI and PlaceholderAPI are soft dependencies. CrucialAPI is auto-downloaded from GitHub releases if missing. PlaceholderAPI features gracefully degrade when absent.

## Project Structure

```
src/main/java/de/chafficplugins/mininglevels/
├── MiningLevels.java                          # Main plugin class (onLoad/onEnable/onDisable, config)
├── api/
│   ├── MiningPlayer.java                      # Player progression data model (UUID, level, XP, rewards)
│   ├── MiningLevel.java                       # Level definition (skills, rewards, commands, level-up logic)
│   ├── MiningBlock.java                       # Block config (materials, XP value, min level required)
│   └── Reward.java                            # ItemStack wrapper for JSON serialization
├── listeners/
│   ├── MiningLevelsCommandListener.java       # /mininglevels command dispatcher
│   ├── RewardCommandListener.java             # /miningrewards command handler
│   ├── commands/
│   │   └── LevelingCommands.java              # Static implementations for setlevel, setxp, level, leaderboard
│   └── events/
│       ├── MiningEvents.java                  # Core mining: block damage/break, XP awards, haste, instabreak, extra ore
│       ├── ServerEvents.java                  # Player join (create profile) / quit (save data)
│       └── NoXpBlockEvents.java               # Tracks player-placed/generated blocks, TNT explosion filtering
├── gui/
│   ├── leaderboard/
│   │   ├── LeaderboardList.java               # Top players GUI
│   │   └── MiningLevelProfile.java            # Individual player stats GUI
│   ├── levels/
│   │   ├── LevelList.java                     # Level management list GUI
│   │   └── LevelEdit.java                     # Level editor GUI (skills, rewards, XP)
│   └── blocks/
│       ├── BlockList.java                     # Block management list GUI
│       └── BlockEdit.java                     # Block editor GUI (materials, XP, min level)
├── io/
│   ├── FileManager.java                       # File creation, downloads, JSON persistence
│   └── MessagesYaml.java                      # messages.yml template creation
├── placeholders/
│   ├── LevelPlaceholders.java                 # PlaceholderAPI expansion (identifier: "ml")
│   └── PlaceholderCommand.java                # Execute commands with placeholder replacement
└── utils/
    ├── ConfigStrings.java                     # All constants: permissions, config keys, message keys, IDs
    ├── CustomMessages.java                    # Localization wrapper (extends CrucialAPI LocalizedFromYaml)
    ├── SenderUtils.java                       # Permission checks, message sending, debug logging
    ├── MathUtils.java                         # randomDouble(min, max) utility
    └── Crucial.java                           # CrucialAPI auto-download and version checking

src/main/resources/
├── plugin.yml                                 # Plugin manifest (uses Maven resource filtering)
└── messages.yml                               # Empty template; defaults set in MessagesYaml.java
```

## Data Storage

All data is stored as JSON files using CrucialAPI's `Json` utility (GSON-based).

| File | Path | Contents |
|---|---|---|
| Player data | `plugins/MiningLevels/data/players.json` | Array of `MiningPlayer` objects (UUID, level ordinal, XP, unclaimed rewards) |
| Level config | `plugins/MiningLevels/config/levels.json` | Array of `MiningLevel` objects (name, nextLevelXP, skills, commands, rewards) |
| Block config | `plugins/MiningLevels/config/blocks.json` | Array of `MiningBlock` objects (materials list, XP, minLevel) |

Level and block JSON files are auto-downloaded from Google Drive on first run if missing. Player data is saved on player quit and plugin disable.

## Configuration (`config.yml`)

| Key | Default | Description |
|---|---|---|
| `levelup_sound` | `ENTITY_PLAYER_LEVELUP` | Sound played on level up (any Bukkit Sound enum name) |
| `max_level_xp_drops` | `false` | Whether players continue earning XP at max level |
| `level_with.player_placed_blocks` | `false` | Whether player-placed blocks grant XP |
| `level_with.generated_blocks` | `false` | Whether world-generated blocks (e.g., cobblestone generators) grant XP |
| `level_progression_messages` | `actionBar` | How progress is displayed: `actionBar`, `chat`, `title`, or `bossBar` |
| `destroy_mining_blocks_on_explode` | `true` | Whether TNT can destroy blocks above the igniter's level |
| `mining_items` | All 6 pickaxe types | List of Material names that count as mining tools for XP |
| `prefix` | `§8[§6ML§8] §r` | Chat message prefix |
| `language` | `CUSTOM` | Localization language identifier |
| `admin.debug` | `false` | Enable debug messages for players with `mininglevels.debug` |

## Commands

### `/mininglevels` (alias: `/ml`)

| Subcommand | Arguments | Permission | Console | Description |
|---|---|---|---|---|
| *(none)* | — | — | No | Opens player's own profile GUI |
| `help` | — | — | Yes | Shows available commands |
| `info` | — | — | Yes | Shows plugin name, version, author |
| `self` | — | — | No | Opens own profile GUI |
| `level` | `<player>` | `mininglevels.level` | Yes | Query a player's current level |
| `leaderboard` | — | `mininglevels.leaderboard` | No | Opens leaderboard GUI (console gets text output) |
| `setlevel` | `<player> <level>` | `mininglevels.setlevel` | Yes | Set a player's mining level |
| `setxp` | `<player> <xp>` | `mininglevels.setxp` | Yes | Set a player's XP (triggers level-up check) |
| `reload` | — | `mininglevels.reload` | Yes | Reload config, levels, blocks, and messages |
| `leveleditor` | — | `mininglevels.editor` | No | Opens level management GUI |
| `blockeditor` | — | `mininglevels.editor` | No | Opens block management GUI |

### `/miningrewards` (alias: `/mr`)

No arguments. Claims all pending level-up rewards into the player's inventory. Not usable from console.

## Permissions

| Permission | Description |
|---|---|
| `mininglevels.*` | Grants all permissions (admin wildcard) |
| `mininglevels.level` | Query other players' levels |
| `mininglevels.setlevel` | Set players' levels |
| `mininglevels.setxp` | Set players' XP |
| `mininglevels.leaderboard` | Access leaderboard |
| `mininglevels.reload` | Reload plugin configuration and data |
| `mininglevels.editor` | Access level and block editor GUIs |
| `mininglevels.debug` | See debug messages (also requires `admin.debug: true` in config) |

Operators and players with `mininglevels.*` bypass all permission checks.

## Game Mechanics

### Mining and XP Flow

1. Player damages a block → `MiningEvents.onBlockDamage()` checks if it's a registered `MiningBlock`
2. If player's level < block's `minLevel`, the event is cancelled and a level-needed message is shown
3. If player has sufficient level and holds a configured mining item:
   - Haste potion effect is applied (5 seconds, amplifier = level's `hasteLevel`)
   - Instant break chance is rolled against `instantBreakProbability`
4. Player breaks the block → `MiningEvents.onBlockBreak()`:
   - Verifies the block isn't in the `noXpBlocks` list (player-placed or generated blocks)
   - Awards XP via `miningPlayer.alterXp(block.getXp())` after a 2-tick delay
   - Rolls extra ore chance against `extraOreProbability`, drops 1 to `maxExtraOre` bonus items

### Level-Up Process (`MiningLevel.levelUp()`)

1. Deducts current level's `nextLevelXP` from player's XP
2. Sets player to the next level
3. Plays configured sound
4. Displays skill change messages (haste, instant break, extra ore, max extra ore) if values differ
5. Lists reward items in chat and adds them to `unclaimedRewards`
6. Executes configured commands (requires PlaceholderAPI)

### Level-Down

If XP goes below 0, the player drops a level and XP wraps to `previousLevel.nextLevelXP + currentXP`.

### Skills (per MiningLevel)

| Skill | Type | Range | Effect |
|---|---|---|---|
| `hasteLevel` | int | 0+ | Haste potion effect amplifier when mining |
| `instantBreakProbability` | float | 0–100 | % chance to instantly break a mining block |
| `extraOreProbability` | float | 0–100 | % chance to receive bonus ore drops |
| `maxExtraOre` | int | 0+ | Maximum number of bonus ore items dropped |

### Block Protection

- **Player-placed blocks:** When `level_with.player_placed_blocks` is `false`, placed mining blocks are tracked and won't grant XP
- **Generated blocks:** When `level_with.generated_blocks` is `false`, blocks formed by world events (e.g., cobblestone generators) are tracked and won't grant XP
- **TNT explosions:** When `destroy_mining_blocks_on_explode` is `false`, blocks requiring a level higher than the TNT igniter's level are removed from the explosion block list

## PlaceholderAPI Integration

Expansion identifier: `ml`. All placeholders use the format `%ml_<key>%`.

### Player Placeholders

| Placeholder | Returns |
|---|---|
| `%ml_player_name%` | Player's username |
| `%ml_level%` | Current level display name |
| `%ml_xp%` | Current XP amount |
| `%ml_xp_needed%` | XP required for next level |
| `%ml_xp_percent%` | XP progress as integer percentage (0–100) |
| `%ml_extra_ore_probability%` | Current extra ore probability |
| `%ml_max_extra_ore%` | Current max extra ore |
| `%ml_instant_break_probability%` | Current instant break probability |
| `%ml_haste_level%` | Current haste level |
| `%ml_rank%` | Player's leaderboard rank |

### Ranked Player Placeholders

Format: `%ml_rank_<N>_<field>%` where N is 1-indexed rank position.

| Placeholder | Returns |
|---|---|
| `%ml_rank_<N>_name%` | Nth ranked player's name |
| `%ml_rank_<N>_level%` | Nth ranked player's level name |
| `%ml_rank_<N>_xp%` | Nth ranked player's XP |
| `%ml_rank_<N>_xpNeeded%` | Nth ranked player's next level XP |
| `%ml_rank_<N>_xpPercent%` | Nth ranked player's XP progress % |

### Command Rewards

Levels can define commands executed on level-up (requires PlaceholderAPI). Two prefixes are supported:

- `console:<command>` — Runs as console (e.g., `console:give %player_name% diamond 1`)
- `player:<command>` — Runs as the player (e.g., `player:msg admin I leveled up!`)

All PlaceholderAPI placeholders are resolved before execution.

## Messages (`messages.yml`)

All user-facing messages are localizable via `messages.yml`. Messages support `{0}`, `{1}`, etc. positional parameters. Default values are set in `MessagesYaml.java` and written to the file on first run.

Key messages: `no_permission`, `new_level`, `player_not_exist`, `xp_received`, `level_of`, `level_unlocked`, `hastelvl_change`, `instant_break_change`, `extra_ore_change`, `max_extra_ore_change`, `rewards_list`, `claim_your_reward`, `level_dropped`, `xp_gained`, `reload_successful`, `error_occurred`, `no_console_command`, `current_level`, `current_xp`, `current_haste_level`, `current_instant_break_level`, `current_extra_ore_level`, `current_max_extra_ore`, `only_blocks_allowed`, `cant_delete_level`, `no_rewards`, `rewards_claimed`, `no_more_space`, `level_needed`, `leaderboard_header`, `usage_set_level`, `usage_set_xp`, `usage_level`, `close`.

## Key Architecture Notes

- **Main class:** `MiningLevels.java` — handles lifecycle (`onLoad`/`onEnable`/`onDisable`), config defaults, and event/command registration
- **Static collections:** `MiningPlayer.miningPlayers`, `MiningLevel.miningLevels`, and `MiningBlock.miningBlocks` are in-memory `ArrayList`s that serve as the data store
- **GUI framework:** All GUIs extend `Page` from CrucialAPI, providing inventory-based UIs
- **Localization:** Uses CrucialAPI's `Localizer` with the identifier `mininglevels`
- **Serialization:** GSON via CrucialAPI's `Json` utility; `Reward` wraps `ItemStack` for JSON compatibility
- **Permission bypass:** `SenderUtils.hasOnePermissions()` grants access to ops and players with `mininglevels.*`
- **bStats ID:** 14709 (anonymous usage statistics)
- **Spigot Resource ID:** 100886
