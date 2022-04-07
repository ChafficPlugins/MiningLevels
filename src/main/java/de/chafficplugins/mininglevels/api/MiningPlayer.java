package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.io.FileManager;
import io.github.chafficui.CrucialAPI.Utils.localization.Localizer;
import io.github.chafficui.CrucialAPI.Utils.player.effects.Interface;
import io.github.chafficui.CrucialAPI.io.Json;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendActionBar;
import static io.github.chafficui.CrucialAPI.Utils.api.Bossbar.sendBossbar;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0.0
 *
 * Contains a player's mining level, its xp and unclaimed rewards.
 */
public class MiningPlayer {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    /**
     * The bukkit player's uuid.
     */
    private final UUID uuid;
    /**
     * The ordinal of the player's mining level.
     */
    private int level;
    /**
     * The amount of xp the player has.
     */
    private int xp;
    /**
     * All currently unclaimed rewards.
     */
    private final ArrayList<ItemStack> unclaimedRewards = new ArrayList<>();

    /**
     * Creates a new miningPlayer from a given uuid.
     * @param uuid The uuid of the player.
     * @param level The level of the player.
     * @param xp The xp of the player.
     * @throws IllegalArgumentException If the player already exists.
     */
    public MiningPlayer(UUID uuid, int level, int xp) {
        this.uuid = uuid;
        this.level = level;
        this.xp = xp;
        if (miningPlayers.contains(this)) {
            throw new IllegalArgumentException("Player already exists!");
        }
        miningPlayers.add(this);
    }

    /**
     * @return The bukkit player's uuid.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * @return The player's mining level.
     */
    public MiningLevel getLevel() {
        return MiningLevel.get(level);
    }

    /**
     * Sets the player's mining level to the given level.
     * @param level The ordinal to set the player's level to.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the player's mining level to the given level.
     * @param level The level to set the player's level to.
     */
    public void setLevel(MiningLevel level) {
        this.level = level.getOrdinal();
    }

    /**
     * The current xp amount of the player.
     * @return The current xp amount of the player.
     */
    public int getXp() {
        return xp;
    }

    /**
     * Alters the player's xp by the given amount and levels up if necessary.
     * @param xp The amount of xp to alter the players xp by.
     */
    public void alterXp(int xp) {
        if(level == MiningLevel.getMaxLevel().getOrdinal() && !plugin.getConfigBoolean(MAX_LEVEL_XP_DROPS)) return;
        this.xp += xp;
        xpChange();
    }

    /**
     * @return The bukkit player of the MiningPlayer.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Calculates if the player needs to level up or not.
     * Sends a status message to the player.
     */
    private void xpChange() {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;
        MiningLevel miningLevel = MiningLevel.miningLevels.get(level);
        if (xp < 0) {
            level--;
            miningLevel = MiningLevel.miningLevels.get(level);
            this.xp = miningLevel.getNextLevelXP() + xp;
            showMessage(LEVEL_DROPPED, ChatColor.RED + String.valueOf(level));
        } else if (xp >= miningLevel.getNextLevelXP() && level + 1 < MiningLevel.miningLevels.size()) {
            getLevel().levelUp(this);
        } else {
            showLevelProgress();
        }
    }

    /**
     * Changes the player's xp to the given amount and checks if the player needs to level up.
     * @param xp The amount of xp to set the player's xp to.
     */
    public void changeXp(int xp) {
        this.xp = xp;
        xpChange();
    }

    /**
     * Sets the player's xp to the given amount without checking if the player needs to level up.
     * @param xp The amount of xp to set the player's xp to.
     */
    public void setXp(int xp) {
        this.xp = xp;
    }

    /**
     * Claims all unclaimed rewards.
     * @return 0 if there are no rewards to claim, 1 if all rewards were claimed, 2 if there were some rewards left to claim.
     */
    public int claim() {
        if(unclaimedRewards.size() > 0) {
            HashMap<Integer, ItemStack> stillUnclaimed = getPlayer().getInventory().addItem(unclaimedRewards.toArray(new ItemStack[0]));
            if(stillUnclaimed.size() > 0) {
                unclaimedRewards.addAll(stillUnclaimed.values());
                return 2;
            } else {
                unclaimedRewards.clear();
                return 1;
            }
        }
        return 0;
    }

    /**
     * Adds the given items to the unclaimed rewards.
     * @param rewards The items to add to the unclaimed rewards.
     */
    public void addRewards(ItemStack... rewards) {
        unclaimedRewards.addAll(List.of(rewards));
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MiningPlayer) {
            return ((MiningPlayer) object).uuid.equals(this.uuid);
        }
        return false;
    }

    //Static
    /**
     * A list of all the players.
     */
    public static ArrayList<MiningPlayer> miningPlayers = new ArrayList<>();

    /**
     * A method to load all the MiningPlayers registered in the file FileManager.PLAYERS, into the static ArrayList miningPlayers.
     * @throws IOException If the file FileManager.PLAYERS is not found.
     */
    public static void init() throws IOException {
        ArrayList<MiningPlayer> mPs = Json.fromJson(FileManager.PLAYERS, new TypeToken<ArrayList<MiningPlayer>>() {
        }.getType());
        if(mPs != null) {
            miningPlayers = mPs;
        }
    }

    /**
     * A method to save all the MiningPlayers in the static ArrayList miningPlayers to the file FileManager.PLAYERS.
     * @throws IOException If the file FileManager.PLAYERS is not found.
     */
    public static void save() throws IOException {
        if (miningPlayers != null) {
            FileManager.saveFile(FileManager.PLAYERS, miningPlayers);
        }
    }

    /**
     * Gets the MiningPlayer with the given UUID.
     * @param uuid The UUID of the player to get.
     * @return The MiningPlayer with the given UUID.
     */
    public static MiningPlayer getMiningPlayer(UUID uuid) {
        for (MiningPlayer miningPlayer : miningPlayers) {
            if(uuid.equals(miningPlayer.uuid)) {
                return miningPlayer;
            }
        }
        return null;
    }

    /**
     * Checks if the given player is a MiningPlayer.
     *
     * @param uuid The UUID of the player to check.
     * @return True if the player is a MiningPlayer, false if not.
     */
    public static boolean notExists(UUID uuid) {
        return getMiningPlayer(uuid) == null;
    }

    public void showMessage(String key, ChatColor color, String... values) {
        String msg = ChatColor.GREEN + Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + key, values);
        switch (plugin.getConfigString(LEVEL_PROGRESSION_MESSAGES)) {
            case "chat" -> getPlayer().sendMessage(msg);
            case "title" -> Interface.showText(getPlayer(), msg, "");
            case "actionBar" -> sendActionBar(getPlayer(), key, color, values);
            case "bossBar" -> sendBossbar(getPlayer(), msg, BarColor.GREEN, 100, 5 * 20);
            default -> plugin.error("Invalid level progression message type: " + plugin.getConfigString(LEVEL_PROGRESSION_MESSAGES));
        }
    }

    public void showLevelProgress() {
        String msg = ChatColor.GREEN + Localizer.getLocalizedString(LOCALIZED_IDENTIFIER + "_" + XP_GAINED, getLevel().getName(), String.valueOf(xp), String.valueOf(getLevel().getNextLevelXP()));
        switch (plugin.getConfigString(LEVEL_PROGRESSION_MESSAGES)) {
            case "chat" -> getPlayer().sendMessage(msg);
            case "title" -> Interface.showText(getPlayer(), msg, "");
            case "actionBar" -> sendActionBar(getPlayer(), XP_GAINED, ChatColor.GREEN, getLevel().getName(), String.valueOf(xp), String.valueOf(getLevel().getNextLevelXP()));
            case "bossBar" -> sendBossbar(getPlayer(), msg, BarColor.GREEN, (int) (((float) xp / (float) getLevel().getNextLevelXP()) * 100), 5 * 20);
            default -> plugin.error("Invalid level progression message type: " + plugin.getConfigString(LEVEL_PROGRESSION_MESSAGES));
        }
    }

    public void showMessage(String key, String... values) {
        showMessage(key, ChatColor.RESET, values);
    }
}
