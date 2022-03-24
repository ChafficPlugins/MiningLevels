package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.io.FileManager;
import io.github.chafficui.CrucialAPI.io.Json;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0.0
 *
 * This class defines a mining level and all its rewards, skills and commands.
 */
public class MiningLevel {
    /**
     * The displayName of the mining level.
     */
    private final String name;
    /**
     * The xp needed to levelUp to {@link MiningLevel#getNext()}.
     */
    private final int nextLevelXP;
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
    private float maxExtraOre = 0;
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
    private final Reward[] rewards = new Reward[0];

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
        ItemStack[] items = new ItemStack[rewards.length];
        for (int i = 0; i < rewards.length; i++) {
            ItemStack item = rewards[i].getItemStack();
            if(item != null) {
                items[i] = item;
            } else {
                return new ItemStack[0];
            }
        }
        return items;
    }

    /**
     * @return The amount of xp needed to levelUp to {@link MiningLevel#getNext()}.
     */
    public int getNextLevelXP() {
        return nextLevelXP;
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
        this.extraOreProbability = extraOreProbability;
    }

    /**
     * @return The maximum amount of dropped extra ores a player with this mining level can receive.
     */
    public float getMaxExtraOre() {
        return maxExtraOre;
    }

    /**
     * Sets the maximum amount of dropped extra ores a player with this mining level can receive.
     */
    public void setMaxExtraOre(float maxExtraOre) {
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
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Your Mininglevel is now " + nextLevel.name + "!"));

        player.sendMessage(ChatColor.WHITE + "Level " + ChatColor.GREEN + nextLevel.name + ChatColor.WHITE + " unlocked!");
        player.sendMessage(ChatColor.WHITE + "-------------");

        //print all skill changes
        double nextHasteLevel = nextLevel.getHasteLevel();
        double nextInstantBreakProbability = nextLevel.getInstantBreakProbability();
        double nextExtraOreProbability = nextLevel.getExtraOreProbability();
        double nextMaxExtraOre = nextLevel.getMaxExtraOre();
        if(nextHasteLevel != hasteLevel) {
            player.sendMessage(ChatColor.WHITE + "Haste Level: " + ChatColor.YELLOW + hasteLevel + ChatColor.WHITE + " -> " + ChatColor.GREEN + nextHasteLevel);
        }
        if(nextInstantBreakProbability != instantBreakProbability) {
            player.sendMessage(ChatColor.WHITE + "Instant Break Probability: " + ChatColor.YELLOW + instantBreakProbability + ChatColor.WHITE + " -> " + ChatColor.GREEN + nextInstantBreakProbability);
        }
        if(nextExtraOreProbability != extraOreProbability) {
            player.sendMessage(ChatColor.WHITE + "Extra Ore Probability: " + ChatColor.YELLOW + extraOreProbability + ChatColor.WHITE + " -> " + ChatColor.GREEN + nextExtraOreProbability);
        }
        if(nextMaxExtraOre != maxExtraOre) {
            player.sendMessage(ChatColor.WHITE + "Max Extra Ore: " + ChatColor.YELLOW + maxExtraOre + ChatColor.WHITE + " -> " + ChatColor.GREEN + nextMaxExtraOre);
        }

        //tell the player all rewards
        if(nextLevel.rewards != null && nextLevel.rewards.length > 0) {
            player.sendMessage("");
            player.sendMessage(ChatColor.WHITE + "Rewards: ");
            for (Reward reward : nextLevel.rewards) {
                player.sendMessage(ChatColor.WHITE + "  " + ChatColor.YELLOW + reward.getName() + ChatColor.WHITE + ": " + ChatColor.GREEN + reward.getAmount());
            }
            miningPlayer.addRewards(nextLevel.getRewards());
            player.sendMessage("Claim your rewards with /miningrewards");
        }

        //perform all commands
        if(nextLevel.commands != null && nextLevel.commands.length > 0) {
            for (String command : nextLevel.commands) {
                player.performCommand(command);
            }
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
}
