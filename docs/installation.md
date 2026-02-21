# Installation

## Download

You can download MiningLevels from:

- [SpigotMC](https://www.spigotmc.org/resources/mininglevels.100886/)
- [GitHub Releases](https://github.com/ChafficPlugins/MiningLevels/releases/latest)

## Setup

1. **Install CrucialLib** – Download [CrucialLib](https://github.com/ChafficPlugins/CrucialLib) and place it in your server's `plugins/` folder. MiningLevels requires this dependency.
2. **Install MiningLevels** – Place the MiningLevels `.jar` file in your server's `plugins/` folder.
3. **Restart the server** – Restart (not reload) your server to let the plugin generate its default configuration files.
4. **Configure** – Edit the generated config files in `plugins/MiningLevels/` to suit your server. See the [Configuration](configuration.md) guide for details.
5. **(Optional) Install PlaceholderAPI** – If you want to use placeholders or level-up commands, install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

## Updating

1. Download the latest version from SpigotMC or GitHub Releases.
2. Replace the old `.jar` file in your `plugins/` folder with the new one.
3. Restart the server.
4. Check the console for any migration messages or new config options.

## File Structure

After first run, MiningLevels creates the following files:

```
plugins/MiningLevels/
├── config.yml          # Main configuration
├── config/
│   ├── levels.json     # Level definitions
│   └── blocks.json     # Mining block definitions
└── data/
    └── players.json    # Player progress data
```

Default `levels.json` and `blocks.json` are bundled inside the plugin JAR and copied automatically on first run. No internet connection is required for setup.

### Player Auto-Registration

Since version 1.3.1, players are automatically registered when they interact with any MiningLevels feature (mining, commands, placeholders, etc.). Previously, if a player was missing from `players.json`, actions like mining or running commands would fail with an error. Now the plugin creates a new player entry (level 0, 0 XP) on the fly and saves it immediately. Players are still also registered on join as before.
