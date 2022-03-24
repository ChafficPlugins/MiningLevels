package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.io.FileManager;
import io.github.chafficui.CrucialAPI.io.Json;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MiningPlayer {
    private final static MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    private final UUID uuid;
    private int level;
    private int xp;
    private final ArrayList<ItemStack> unclaimedRewards = new ArrayList<>();

    public MiningPlayer(UUID uuid, int level, int xp) {
        this.uuid = uuid;
        this.level = level;
        this.xp = xp;
        if (miningPlayers.contains(this)) {
            throw new IllegalArgumentException("Player already exists!");
        }
        miningPlayers.add(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    public MiningLevel getLevel() {
        return MiningLevel.get(level);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevel(MiningLevel level) {
        this.level = level.getOrdinal();
    }

    public int getXp() {
        return xp;
    }

    public void alterXp(int xp) {
        this.xp += xp;
        xpChange(this.xp);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    private void xpChange(int xp) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;
        MiningLevel miningLevel = MiningLevel.miningLevels.get(level);
        if (xp < 0) {
            level--;
            miningLevel = MiningLevel.miningLevels.get(level);
            this.xp = miningLevel.getNextLevelXP() + xp;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Your Mininglevel dropped to " + level + "!"));
        } else if (xp >= miningLevel.getNextLevelXP() && level + 1 < MiningLevel.miningLevels.size()) {
            getLevel().levelUp(this, player);
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Level " + getLevel().getName() + ": [" + xp + "/" + getLevel().getNextLevelXP() + "]"));
        }
    }

    public void changeXp(int xp) {
        this.xp = xp;
        xpChange(xp);
    }

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
    public static ArrayList<MiningPlayer> miningPlayers = new ArrayList<>();

    public static void init() throws IOException {
        ArrayList<MiningPlayer> mPs = Json.fromJson(FileManager.PLAYERS, new TypeToken<ArrayList<MiningPlayer>>() {
        }.getType());
        if(mPs != null) {
            miningPlayers = mPs;
        }
    }

    public static void reload() throws IOException {
        ArrayList<MiningPlayer> mPs = Json.fromJson(FileManager.PLAYERS, new TypeToken<ArrayList<MiningPlayer>>() {
        }.getType());
        if(mPs == null) mPs = new ArrayList<>();
        for (MiningPlayer miningPlayer : miningPlayers) {
            if(!mPs.contains(miningPlayer)) {
                mPs.add(miningPlayer);
            }
        }

        FileManager.saveFile(FileManager.PLAYERS, mPs);
        init();
    }

    public static void save() throws IOException {
        if (miningPlayers != null) {
            FileManager.saveFile(FileManager.PLAYERS, miningPlayers);
        }
    }

    public static MiningPlayer getMiningPlayer(UUID uuid) {
        for (MiningPlayer miningPlayer : miningPlayers) {
            if(uuid.equals(miningPlayer.uuid)) {
                return miningPlayer;
            }
        }
        return null;
    }

    public static boolean notExists(UUID uuid) {
        return getMiningPlayer(uuid) == null;
    }
}
