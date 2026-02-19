# Runbook — MiningLevels

## Release Process

1. Update version in `pom.xml`
2. Update version in `plugin.yml`
3. Run `mvn clean verify` — ensure all tests pass
4. Build the shaded JAR: `mvn package`
5. Upload `target/MiningLevels-<version>.jar` to GitHub Releases / SpigotMC

## Adding a New Mining Block

1. In-game: Use `/mininglevels editor` → Blocks → Add Block
2. Or edit `plugins/MiningLevels/miningBlocks.json` directly:
   ```json
   {
     "materials": ["COPPER_ORE", "DEEPSLATE_COPPER_ORE"],
     "xp": 2,
     "minLevel": 0
   }
   ```
3. Reload with `/mininglevels reload`

## Adding a New Mining Level

1. In-game: Use `/mininglevels editor` → Levels → Add Level
2. Or edit `plugins/MiningLevels/miningLevels.json`:
   ```json
   {
     "name": "Grandmaster",
     "nextLevelXP": 2000,
     "ordinal": 4,
     "hasteLevel": 3,
     "instantBreakProbability": 15.0,
     "extraOreProbability": 20.0,
     "maxExtraOre": 3,
     "rewards": [],
     "commands": []
   }
   ```
3. Levels must have sequential ordinals starting from 0
4. Reload with `/mininglevels reload`

## Common Issues

### "Error 24: Failed to download CrucialLib"
- The server couldn't download CrucialLib automatically
- Download manually from: https://github.com/ChafficPlugins/CrucialLib/releases
- Place the JAR in the `plugins/` folder and restart

### "Error 26: Wrong version of CrucialLib"
- Incompatible CrucialLib version installed
- Required version: 3.0.x (see `ConfigStrings.CRUCIAL_LIB_VERSION`)
- Download the correct version and replace the existing JAR

### Players not earning XP
- Check that the block is in `miningBlocks.json`
- Check that the player's pickaxe is in the `mining_items` config list
- Check `level_with.player_placed_blocks` if mining player-placed blocks
- Check that the player meets the block's `minLevel` requirement

### PlaceholderAPI placeholders not working
- Ensure PlaceholderAPI is installed and enabled
- Placeholders: `%mininglevels_level%`, `%mininglevels_xp%`, `%mininglevels_next_level_xp%`

## Configuration Reference

Key config values in `config.yml`:

| Key | Default | Description |
|-----|---------|-------------|
| `levelup_sound` | `ENTITY_PLAYER_LEVELUP` | Sound played on level-up |
| `max_level_xp_drops` | `false` | Allow XP at max level |
| `level_with.player_placed_blocks` | `false` | Earn XP from placed blocks |
| `level_with.generated_blocks` | `false` | Earn XP from generated blocks |
| `level_progression_messages` | `actionBar` | Message type: chat/title/actionBar/bossBar |
| `destroy_mining_blocks_on_explode` | `true` | Track exploded blocks |
| `mining_items` | All pickaxes | Tools that grant mining XP |
