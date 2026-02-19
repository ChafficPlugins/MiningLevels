# Commands & Permissions

## Commands

### Reward Commands

| Command | Description | Permission |
|---|---|---|
| `/mr` or `/miningrewards` | Claim mining level rewards | None |

### Level Commands

| Command | Description | Permission |
|---|---|---|
| `/ml` or `/ml self` | Shows your mining level and XP | None |
| `/ml level <player>` | Shows the mining level of a player | `mininglevels.level` |
| `/ml setlevel <player> <level>` | Set the mining level of a player | `mininglevels.setlevel` |
| `/ml setxp <player> <amount>` | Set the mining XP of a player | `mininglevels.setxp` |
| `/ml leaderboard` | Shows the five highest level players | `mininglevels.leaderboard` |

### Editor Commands

| Command | Description | Permission |
|---|---|---|
| `/ml leveleditor` | Opens the level editor GUI | `mininglevels.editor` |
| `/ml blockeditor` | Opens the block editor GUI | `mininglevels.editor` |

### Plugin Commands

| Command | Description | Permission |
|---|---|---|
| `/ml reload` | Reloads MiningLevels configuration | `mininglevels.reload` |
| `/ml info` | Shows information about MiningLevels | None |

## Permissions

| Permission | Description | Default |
|---|---|---|
| `mininglevels.level` | View another player's mining level | Op |
| `mininglevels.setlevel` | Set a player's mining level | Op |
| `mininglevels.setxp` | Set a player's mining XP | Op |
| `mininglevels.leaderboard` | View the mining leaderboard | Op |
| `mininglevels.editor` | Access the level and block editors | Op |
| `mininglevels.reload` | Reload the plugin configuration | Op |
| `mininglevels.debug` | Receive debug messages when `admin.debug` is enabled | Op |
