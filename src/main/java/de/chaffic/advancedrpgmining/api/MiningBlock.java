package de.chaffic.advancedrpgmining.api;

import com.google.gson.reflect.TypeToken;
import de.chaffic.advancedrpgmining.io.FileManager;
import de.chaffic.advancedrpgmining.io.Json;
import org.bukkit.Material;

import java.io.IOException;
import java.util.ArrayList;

public class MiningBlock {
    private final String material;
    private final int xp;
    private final int minLevel;

    public MiningBlock(Material material, int xp, int minLevel) {
        if (material.isBlock()) {
            this.material = material.name();
            this.xp = xp;
            this.minLevel = minLevel;
        } else {
            throw new IllegalArgumentException(material.name() + " is not a block type.");
        }
    }

    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    public int getXp() {
        return xp;
    }

    public int getMinLevel() {
        return minLevel;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof MiningBlock) {
            return ((MiningBlock) object).material.equals(this.material);
        }
        return false;
    }

    //Static
    public static ArrayList<MiningBlock> miningBlocks;

    public static void init() throws IOException {
        miningBlocks = Json.loadFile(FileManager.BLOCKS, new TypeToken<ArrayList<MiningBlock>>() {
        }.getType());
    }

    public static void save() throws IOException {
        if (miningBlocks != null) {
            Json.saveFile(FileManager.BLOCKS, miningBlocks);
        }
    }

    public static MiningBlock getMiningBlock(Material material) {
        for (MiningBlock miningBlock : miningBlocks) {
            if (miningBlock.getMaterial().equals(material)) {
                return miningBlock;
            }
        }
        return null;
    }
}
