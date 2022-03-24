package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.io.FileManager;
import io.github.chafficui.CrucialAPI.io.Json;
import org.bukkit.Material;

import java.io.IOException;
import java.util.ArrayList;

public class MiningBlock {
    private final ArrayList<String> materials = new ArrayList<>();
    private final int xp;
    private final int minLevel;

    public MiningBlock(Material material, int xp, int minLevel) {
        if (material.isBlock()) {
            this.materials.add(material.name());
            this.xp = xp;
            this.minLevel = minLevel;
        } else {
            throw new IllegalArgumentException(material.name() + " is not a block type.");
        }
    }

    public MiningBlock(Material[] materials, int xp, int minLevel) {
        for (Material m : materials) {
            if (m.isBlock()) {
                this.materials.add(m.name());
            } else {
                throw new IllegalArgumentException(m.name() + " is not a block type.");
            }
        }

        this.xp = xp;
        this.minLevel = minLevel;
    }

    public ArrayList<Material> getMaterials() {
        ArrayList<Material> materials = new ArrayList<>();
        for (String material : this.materials) {
            materials.add(Material.valueOf(material));
        }
        return materials;
    }

    public int getXp() {
        return xp;
    }

    public int getMinLevel() {
        return minLevel;
    }

    //Static
    public static ArrayList<MiningBlock> miningBlocks = new ArrayList<>();

    public static void init() throws IOException {
        miningBlocks = Json.fromJson(FileManager.BLOCKS, new TypeToken<ArrayList<MiningBlock>>() {
        }.getType());
    }

    public static void reload() throws IOException {
        init();
    }

    public static void save() throws IOException {
        if (miningBlocks != null) {
            FileManager.saveFile(FileManager.BLOCKS, miningBlocks);
        }
    }

    public static MiningBlock getMiningBlock(Material material) {
        for (MiningBlock miningBlock : miningBlocks) {
            if (miningBlock.getMaterials().contains(material)) {
                return miningBlock;
            }
        }
        return null;
    }
}
