package de.chaffic.advancedrpgmining.api;

import com.google.gson.reflect.TypeToken;
import de.chaffic.advancedrpgmining.io.FileManager;
import de.chaffic.advancedrpgmining.io.Json;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MiningPlayer {
    private final UUID uuid;
    private int level;
    private int xp;

    public MiningPlayer(UUID uuid, int level, int xp) {
        this.uuid = uuid;
        this.level = level;
        this.xp = xp;
        if(miningPlayers.contains(this)) {
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

    public int getXp() {
        return xp;
    }

    public void alterXp(int xp) {
        this.xp += xp;
        xpChange(this.xp);
    }

    private void xpChange(int xp) {
        Player player = Bukkit.getPlayer(uuid);
        if (xp < 0) {
            this.xp = 0;
            level--;
            if(player != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Your Mininglevel dropped to " + level + "!"));
            }
        } else if (xp >= MiningLevel.miningLevels.get(level).getNextLevelXP()) {
            this.xp = 0;
            level++;
            if(player != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Your Mininglevel is now " + level + "!"));
                player.sendMessage(ChatColor.WHITE + "Level " + ChatColor.GREEN + level);
                player.sendMessage(ChatColor.WHITE + "-------------");
                player.sendMessage("Haste Level: " + getLevel().getHasteLevel());
                player.sendMessage("Instant Break Probability: " + getLevel().getInstantBreakProbability());
                player.sendMessage("Extra Drops Probability: " + getLevel().getExtraOreProbability());
                player.sendMessage("Max Extra Drops: " + getLevel().getMaxExtraOre());
                player.sendMessage("Xp To Next Level: " + getLevel().getNextLevelXP());
            }
        } else {
            if(player != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "Level: " + level + "[" + xp + "/" + getLevel().getNextLevelXP() + "]"));
            }
        }
    }

    public void setXp(int xp) {
        this.xp = xp;
        xpChange(xp);
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
        ArrayList<MiningPlayer> mPs = Json.loadFile(FileManager.PLAYERS, new TypeToken<ArrayList<MiningPlayer>>() {
        }.getType());
        if(mPs != null) {
            miningPlayers = mPs;
        }
    }

    public static void save() throws IOException {
        if (miningPlayers != null) {
            Json.saveFile(FileManager.PLAYERS, miningPlayers);
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

    public static boolean exists(UUID uuid) {
        return getMiningPlayer(uuid) != null;
    }
}
