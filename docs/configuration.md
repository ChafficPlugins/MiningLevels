# Configuration

MiningLevels uses three configuration files: `config.yml` for general settings, `config/levels.json` for level definitions, and `config/blocks.json` for mining block definitions.

## config.yml

### Options

#### `admin.debug`

If enabled, all admins and players with the `mininglevels.debug` permission will receive debug messages for plugin events they are involved in.

- Type: `boolean`
- Default: `false`

#### `destroy_mining_blocks_on_explode`

Decides if players can destroy blocks that require a higher level using TNT explosions initiated by the player.

- Type: `boolean`
- Default: `false`

#### `levelup_sound`

The sound that plays whenever a player levels up. Must be a valid [Bukkit Sound](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html).

- Type: `string`
- Default: `ENTITY_PLAYER_LEVELUP`

#### `max_level_xp_drops`

Decides if a player will continue gaining XP after reaching the highest level.

- Type: `boolean`
- Default: `false`

#### `level_with.player_placed_blocks`

Decides if a player can earn XP by breaking blocks placed by themselves or another player.

- Type: `boolean`
- Default: `false`

#### `level_with.generated_blocks`

Decides if a player can earn XP by breaking blocks generated with a cobblestone generator.

- Type: `boolean`
- Default: `false`

#### `level_progression_messages`

Sets where level progression messages are shown. Options: `chat`, `title`, `actionBar`, or `bossBar`.

- Type: `string`
- Default: `actionBar`

#### `mining_items`

Defines which items count as mining tools for XP and effects. Must be valid [Bukkit Materials](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).

- Type: `list`
- Default: All pickaxe types

#### `prefix`

The chat prefix used for plugin messages.

- Type: `string`
- Default: `§8[§6ML§8] §r`

#### `language`

Sets the language of the plugin. Currently can only be set to `CUSTOM`.

- Type: `string`
- Default: `CUSTOM`

### Default config.yml

```yaml
admin:
  debug: false
destroy_mining_blocks_on_explode: false
levelup_sound: ENTITY_PLAYER_LEVELUP
max_level_xp_drops: false
level_with:
  player_placed_blocks: false
  generated_blocks: false
level_progression_messages: actionBar
mining_items:
- DIAMOND_PICKAXE
- GOLDEN_PICKAXE
- IRON_PICKAXE
- STONE_PICKAXE
- WOODEN_PICKAXE
- NETHERITE_PICKAXE
prefix: §8[§6ML§8] §r
language: CUSTOM
```

---

## Mining Levels (`config/levels.json`)

Players level up by mining blocks to gain XP. Levels are defined in `config/levels.json` and **must be in sorted order** by ordinal (from first to last level).

**Tip:** Use the in-game level editor (`/ml leveleditor`) to edit levels instead of manually editing JSON.

### Level Properties

| Property | Type | Description |
|---|---|---|
| `name` | string | The display name of the level |
| `ordinal` | integer | The number of the level, starting at 0 (level 1 = ordinal 0) |
| `nextLevelXP` | integer | XP needed to reach the next level |
| `instantBreakProbability` | float | Probability (%) that a player at this level will break a block instantly |
| `extraOreProbability` | float | Probability (%) that extra ores will drop when a player breaks a block |
| `maxExtraOre` | float | Maximum amount of extra ores that can drop |
| `hasteLevel` | integer | Haste effect level applied while mining a mining block |
| `commands` | list | Commands executed when a player reaches this level (requires PlaceholderAPI) |
| `rewards` | list | Item rewards given when a player reaches this level |

### Commands

Since version 1.1.0, levels support commands that run on level-up. **PlaceholderAPI is required.**

Since version 1.2.9, commands can be prefixed with `console:` or `player:` to control who executes them:

```json
"commands": [
  "console:op %ml_player_name%",
  "player:say Hey guys I %ml_player_name% reached %ml_level% in Mining!"
]
```

### Rewards

Since version 1.1.0, levels support item rewards. Each reward requires at minimum a `type` field, which must be a valid [Bukkit Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html).

```json
"rewards": [
  {
    "type": "DIAMOND",
    "amount": 10
  }
]
```

### Example levels.json

```json
[
  {
    "name": "1",
    "nextLevelXP": 100,
    "ordinal": 0,
    "instantBreakProbability": 0.0,
    "extraOreProbability": 0.0,
    "maxExtraOre": 0.0,
    "hasteLevel": 0,
    "commands": [
      "console:say wow, next level :)"
    ],
    "rewards": [
      {
        "type": "DIAMOND",
        "amount": 10
      }
    ]
  },
  {
    "name": "2",
    "nextLevelXP": 300,
    "ordinal": 1,
    "instantBreakProbability": 5.0,
    "extraOreProbability": 10.0,
    "maxExtraOre": 0.0,
    "hasteLevel": 0,
    "commands": [],
    "rewards": []
  }
]
```

Each level must be separated by a comma. **Levels must be sorted by ordinal.**

---

## Mining Blocks (`config/blocks.json`)

Mining blocks define which blocks give XP and which level is required to mine them. Configured in `config/blocks.json`.

**Tip:** Use the in-game block editor (`/ml blockeditor`) to edit blocks instead of manually editing JSON.

### Block Properties

| Property | Type | Description |
|---|---|---|
| `materials` | list | One or more [Bukkit Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) names. Only block materials are valid. |
| `xp` | integer | XP amount (greater than 0) a player receives when breaking this block |
| `minLevel` | integer | The ordinal number of the minimum level required to mine this block. Set to 0 if all players should be able to mine it. |

### Example blocks.json

```json
[
  {
    "materials": [
      "DEEPSLATE_REDSTONE_ORE",
      "REDSTONE_ORE"
    ],
    "xp": 1,
    "minLevel": 0
  },
  {
    "materials": [
      "DEEPSLATE_LAPIS_ORE",
      "LAPIS_ORE"
    ],
    "xp": 1,
    "minLevel": 3
  }
]
```

Each block entry must be separated by a comma.

---

## Placeholders

MiningLevels provides PlaceholderAPI placeholders. **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) must be installed.**

### Player Placeholders

| Placeholder | Description | Since |
|---|---|---|
| `%ml_player_name%` | Current name of the player | 1.2.9 |
| `%ml_level%` | Current mining level of the player | — |
| `%ml_xp%` | Current XP amount the player has | — |
| `%ml_xp_needed%` | XP needed to reach the next level | — |
| `%ml_xp_percent%` | Percentage of XP collected toward the next level | — |
| `%ml_extra_ore_probability%` | Current probability for extra ore drops | — |
| `%ml_instant_break_probability%` | Current probability for instant block break | — |
| `%ml_max_extra_ore%` | Maximum amount of extra ores that can drop | — |
| `%ml_haste_level%` | Haste level applied while mining | — |
| `%ml_rank%` | Player's rank on the leaderboard | — |

### Leaderboard Placeholders

These placeholders display info about ranked players. Replace `1` with any rank number.

| Placeholder | Description |
|---|---|
| `%ml_rank_1_name%` | Name of the player at rank 1 |
| `%ml_rank_1_xp%` | XP of the player at rank 1 |
| `%ml_rank_1_xpPercent%` | XP percentage toward next level for the player at rank 1 |
| `%ml_rank_1_xpNeeded%` | XP needed for next level for the player at rank 1 |
| `%ml_rank_1_level%` | Level of the player at rank 1 |
