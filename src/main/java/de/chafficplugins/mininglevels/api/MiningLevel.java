package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.io.FileManager;
import de.chafficplugins.mininglevels.placeholders.PlaceholderCommand;
import io.github.chafficui.CrucialAPI.io.Json;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendMessage;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0.0
 *
 * This class defines a mining level and all its rewards, skills and commands.
 */
public class MiningLevel {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    /**
     * The displayName of the mining level.
     */
    private final String name;
    /**
     * The xp needed to levelUp to {@link MiningLevel#getNext()}.
     */
    private int nextLevelXP;
    /**
     * The place this level takes in the list.
     */
    private final int ordinal;
    /**
     * The probability a player with this MiningLevel will break a MiningBlock instantly.
     */
    private float instantBreakProbability = 0;
    /**
     * The probability a player with this MiningLevel will receive extra ores when breaking a MiningBlock.
     */
    private float extraOreProbability = 0;
    /**
     * The maximum amount of dropped extra ores a player with this MiningLevel can receive.
     */
    private int maxExtraOre = 0;
    /**
     * The hasteLevel a player will receive when damaging a MiningBlock with this MiningLevel.
     */
    private int hasteLevel = 0;
    /**
     * The commands that will be called whenever a player levels up to this MiningLevel.
     */
    private final String[] commands = new String[0];
    /**
     * The items a player will be able to claim with the claim command, when he has leveled up to this MiningLevel.
     */
    private ArrayList<ItemStack> rewards = new ArrayList<>();

    /**
     * Initializes the MiningLevels.
     * @param name The displayName of the MiningLevel.
     * @param nextLevelXP The xp needed to levelUp to {@link MiningLevel#getNext()}.
     * @param ordinal The place this level takes in the list.
     */
    public MiningLevel(String name, int nextLevelXP, int ordinal) {
        this.name = name;
        this.nextLevelXP = nextLevelXP;
        this.ordinal = ordinal;
    }

    /**
     * @return The displayName of the MiningLevel.
     */
    public String getName() {
        return name;
    }

    /**
     * Creates an Array of ItemStacks from the rewards.
     * @return An Array of ItemStacks.
     */
    public ItemStack[] getRewards() {
        return rewards.toArray(new ItemStack[0]);
    }

    /**
     * Adds an ItemStack as a reward.
     * @param item The ItemStack to add.
     */
    public void addReward(ItemStack item) {
        rewards.add(item);
    }

    /**
     * Sets the rewards.
     * @param rewards The new rewards.
     */
    public void setRewards(ArrayList<ItemStack> rewards) {
        this.rewards = rewards;
    }

    /**
     * @return The amount of xp needed to levelUp to {@link MiningLevel#getNext()}.
     */
    public int getNextLevelXP() {
        return nextLevelXP;
    }

    /**
     * Sets the xp needed to levelUp to {@link MiningLevel#getNext()}.
     * @param nextLevelXP The xp needed to levelUp to {@link MiningLevel#getNext()}.
     */
    public void setNextLevelXP(int nextLevelXP) {
        this.nextLevelXP = nextLevelXP;
    }

    /**
     * @return The place this level takes in the list.
     */
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * @return The probability a player with this mining level will break a MiningBlock instantly.
     */
    public float getInstantBreakProbability() {
        return instantBreakProbability;
    }

    /**
     * Sets the probability a player with this mining level will break a MiningBlock instantly.
     */
    public void setInstantBreakProbability(float instantBreakProbability) {
        if(instantBreakProbability < 0 || instantBreakProbability > 100) {
            return;
        }
        this.instantBreakProbability = instantBreakProbability;
    }

    /**
     * @return The probability a player with this mining level will receive extra ores when breaking a MiningBlock.
     */
    public float getExtraOreProbability() {
        return extraOreProbability;
    }

    /**
     * Sets the probability a player with this mining level will receive extra ores when breaking a MiningBlock.
     */
    public void setExtraOreProbability(float extraOreProbability) {
        if(extraOreProbability < 0 || extraOreProbability > 100) {
            return;
        }
        this.extraOreProbability = extraOreProbability;
    }

    /**
     * @return The maximum amount of dropped extra ores a player with this mining level can receive.
     */
    public int getMaxExtraOre() {
        return maxExtraOre;
    }

    /**
     * Sets the maximum amount of dropped extra ores a player with this mining level can receive.
     */
    public void setMaxExtraOre(int maxExtraOre) {
        if(maxExtraOre < 0) {
            return;
        }
        this.maxExtraOre = maxExtraOre;
    }

    /**
     * @return The hasteLevel a player will receive when damaging a MiningBlock with this MiningLevel.
     */
    public int getHasteLevel() {
        return hasteLevel;
    }

    /**
     * Sets the hasteLevel a player will receive when damaging a MiningBlock with this MiningLevel.
     */
    public void setHasteLevel(int hasteLevel) {
        if(hasteLevel < 0) {
            return;
        }
        this.hasteLevel = hasteLevel;
    }

    /**
     * @return The level that is ranked before this level. If this is the first level, returns this.
     */
    public MiningLevel getBefore() {
        if (ordinal == 0) return get(0);
        return get(ordinal - 1);
    }

    /**
     * @return The level that is ranked after this level. If this is the last level, returns this.
     */
    public MiningLevel getNext() {
        if (ordinal >= miningLevels.size() - 1)
            return get(miningLevels.size() - 1);
        return get(ordinal + 1);
    }

    /**
     * Levels the given player up to the next MiningLevel.
     * If the player is already at the highest level, nothing happens.
     * Displays all rewards and skill changes to the player and performs all commands.
     * @param miningPlayer The player to level up.
     */
    public void levelUp(MiningPlayer miningPlayer) {
        Player player = miningPlayer.getPlayer();
        //If the player is already at the highest level, do nothing.
        if (ordinal + 1 >= MiningLevel.miningLevels.size()) {
            return;
        }


        miningPlayer.setXp(miningPlayer.getXp() - nextLevelXP);
        MiningLevel nextLevel = getNext();
        miningPlayer.setLevel(nextLevel);
        player.playSound(player.getLocation(), MiningLevels.lvlUpSound, 1, 1);
        miningPlayer.showMessage(NEW_LEVEL, ChatColor.GREEN, nextLevel.name);

        sendMessage(player, LEVEL_UNLOCKED, ChatColor.WHITE, ChatColor.GREEN + nextLevel.name + ChatColor.WHITE);
        player.sendMessage(ChatColor.WHITE + "-------------");

        //print all skill changes
        double nextHasteLevel = nextLevel.getHasteLevel();
        double nextInstantBreakProbability = nextLevel.getInstantBreakProbability();
        double nextExtraOreProbability = nextLevel.getExtraOreProbability();
        double nextMaxExtraOre = nextLevel.getMaxExtraOre();
        sendUpgrade(player, hasteLevel, nextHasteLevel, HASTELVL_CHANGE);
        sendUpgrade(player, instantBreakProbability, nextInstantBreakProbability, INSTANT_BREAK_CHANGE);
        sendUpgrade(player, extraOreProbability, nextExtraOreProbability, EXTRA_ORE_CHANGE);
        sendUpgrade(player, maxExtraOre, nextMaxExtraOre, MAX_EXTRA_ORE_CHANGE);

        //tell the player all rewards
        if(nextLevel.rewards != null && nextLevel.rewards.size() > 0) {
            player.sendMessage("");
            sendMessage(player, REWARDS_LIST, ChatColor.WHITE);
            for (ItemStack reward : nextLevel.rewards) {
                player.sendMessage(ChatColor.WHITE + "  " + ChatColor.YELLOW + getName(reward) + ChatColor.WHITE + ": " + ChatColor.GREEN + reward.getAmount());
            }
            miningPlayer.addRewards(nextLevel.getRewards());
            sendMessage(player, CLAIM_YOUR_REWARD, ChatColor.WHITE);
        }

        //perform all commands
        if(nextLevel.commands != null && nextLevel.commands.length > 0 && plugin.placeholderAPI) {
            for (String command : nextLevel.commands) {
                PlaceholderCommand.perform(player, command);
            }
        }
    }

    private static void sendUpgrade(Player player, double now, double next, String key) {
        if(next != now) {
            sendMessage(player, key, ChatColor.WHITE, ChatColor.YELLOW + String.valueOf(now) + ChatColor.WHITE, ChatColor.GREEN + String.valueOf(next));
        }
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof MiningLevel) {
            return ((MiningLevel) object).ordinal == this.ordinal;
        }
        return false;
    }

    //Static
    /**
     * A list of all registered MiningLevels.
     */
    public static ArrayList<MiningLevel> miningLevels = new ArrayList<>();


    /**
     * A method to load all MiningLevels registered in the file FileManager.LEVELS, into the static ArrayList miningLevels.
     * @throws IOException If the file FileManager.LEVELS is not found.
     */
    public static void init() throws IOException {
        miningLevels = Json.fromJson(FileManager.LEVELS, new TypeToken<ArrayList<MiningLevel>>() {
        }.getType());
    }

    /**
     * A method to save all MiningLevels registered in the static ArrayList miningLevels into the file FileManager.LEVELS.
     * @throws IOException If the file FileManager.LEVELS is not found.
     */
    public static void save() throws IOException {
        if(miningLevels != null) {
            FileManager.saveFile(FileManager.LEVELS, miningLevels);
        }
    }

    /**
     * Does the same as {@link #init()}.
     * @throws IOException If the file FileManager.LEVELS is not found.
     */
    public static void reload() throws IOException {
        init();
    }

    /**
     * A method to get a MiningLevel by its ordinal.
     * @param ordinal The ordinal of the MiningLevel. Null if the ordinal is not found.
     */
    public static MiningLevel get(int ordinal) {
        for(MiningLevel miningLevel : miningLevels) {
            if(miningLevel.ordinal == ordinal) {
                return miningLevel;
            }
        }
        return null;
    }

    /**
     * Returns the material name or the displayName of an item.
     */
    public static String getName(ItemStack itemStack) {
        if(itemStack.getType().name().equals("AIR")) {
            return "AIR";
        }
        if(itemStack.hasItemMeta()) {
            if(itemStack.getItemMeta().hasDisplayName()) {
                return itemStack.getItemMeta().getDisplayName();
            }
        }
        return itemStack.getType().name();
    }

    /**
     * A method to get the maximum level
     * @return The maximum level
     */
    public static MiningLevel getMaxLevel() {
        return MiningLevel.miningLevels.get(MiningLevel.miningLevels.size() - 1);
    }
}
