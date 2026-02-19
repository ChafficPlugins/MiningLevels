# ADR-001: Update to Paper 1.21 and Java 21

## Status
Accepted

## Context
MiningLevels was targeting Spigot 1.15 API with Java 8/14. The Minecraft server ecosystem has moved to 1.21.x, and MockBukkit 4.x (needed for testing) requires Paper API.

## Decision
- Update to **Paper API 1.21** (superset of Spigot API)
- Update to **Java 21** (current LTS)
- Migrate from **CrucialAPI 2.1.7** to **CrucialLib 3.0.0** (package rename: `CrucialAPI` → `CrucialLib`)
- Update `plugin.yml` api-version from `1.15` to `1.21`

## Key Changes
- `PotionEffectType.FAST_DIGGING` → `PotionEffectType.HASTE` (renamed in 1.20+)
- `new URL()` → `URI.create().toURL()` (URL constructor deprecated)
- `Server.checkCompatibility()` updated for 1.21
- All CrucialAPI imports changed to CrucialLib package paths

## Consequences
- Plugin requires Java 21+ to run
- Compatible with Paper/Spigot 1.21.x servers
- Enables use of MockBukkit 4.x for testing
- Some deprecation warnings remain (Sound.valueOf, getPluginLoader) but are functional
