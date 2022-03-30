package de.chafficplugins.mininglevels.placeholders;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class LevelPlaceholders extends PlaceholderExpansion {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    @Override
    public @NotNull String getIdentifier() {
        return "ml";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(player.getUniqueId());
        if(miningPlayer == null) return "error";
        switch (identifier) {
            case "level" -> {
                return miningPlayer.getLevel().getName();
            }
            case "xp" -> {
                return String.valueOf(miningPlayer.getXp());
            }
            case "xp_needed" -> {
                return String.valueOf(miningPlayer.getLevel().getNextLevelXP());
            }
            case "extra_ore_probability" -> {
                return String.valueOf(miningPlayer.getLevel().getExtraOreProbability());
            }
            case "max_extra_ore" -> {
                return String.valueOf(miningPlayer.getLevel().getMaxExtraOre());
            }
            case "instant_break_probability" -> {
                return String.valueOf(miningPlayer.getLevel().getInstantBreakProbability());
            }
            case "haste_level" -> {
                return String.valueOf(miningPlayer.getLevel().getHasteLevel());
            }
            default -> {
                return null;
            }
        }
    }
}
