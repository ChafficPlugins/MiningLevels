package de.chafficplugins.mininglevels.placeholders;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.listeners.commands.LevelingCommands;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.chafficplugins.mininglevels.api.MiningPlayer.getSortedPlayers;

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
            case "xp_percent" -> {
                float percent = (float) miningPlayer.getXp() / (float) miningPlayer.getLevel().getNextLevelXP();
                return String.valueOf((int) (percent * 100));
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
                if(identifier.startsWith("rank")) {
                    List<MiningPlayer> miningPlayers = getSortedPlayers();
                    if(identifier.equals("rank")) {
                        return String.valueOf(miningPlayers.indexOf(miningPlayer) + 1);
                    } else if(identifier.split("_").length == 3) {
                        try {
                            int index = Integer.parseInt(identifier.split("_")[1]);
                            if(index > 0 && index <= miningPlayers.size()) {
                                MiningPlayer rankedPlayer = miningPlayers.get(index - 1);
                                if(rankedPlayer == null) return "error";
                                switch (identifier.split("_")[2]) {
                                    case "level" -> {
                                        return rankedPlayer.getLevel().getName();
                                    }
                                    case "xp" -> {
                                        return String.valueOf(rankedPlayer.getXp());
                                    }
                                    case "xp_needed" -> {
                                        return String.valueOf(rankedPlayer.getLevel().getNextLevelXP());
                                    }
                                    case "xp_percent" -> {
                                        float percent = (float) rankedPlayer.getXp() / (float) rankedPlayer.getLevel().getNextLevelXP();
                                        return String.valueOf((int) (percent * 100));
                                    }
                                    case "name" -> {
                                        return rankedPlayer.getOfflinePlayer().getName();
                                    }
                                    default -> {
                                        return "error";
                                    }
                                }
                            } else {
                                return "error";
                            }
                        } catch (NumberFormatException e) {
                            return "error";
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
    }
}
