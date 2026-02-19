package de.chafficplugins.mininglevels.api;

import com.google.gson.reflect.TypeToken;
import de.chafficplugins.mininglevels.io.FileManager;
import io.github.chafficui.CrucialLib.io.Json;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Chaffic
 * @since 1.0.0
 * @version 1.0.0
 *
 * This class defines how many xp will be given a player when he mines a certain block.
 * It also defines the level needed to mine it.
 */
public class MiningBlock {
    /**
     * The materials that will give the certain xp amount and need the level to be mined.
     */
    private ArrayList<String> materials = new ArrayList<>();
    /**
     * The xp amount that will be given when the block is mined.
     */
    private int xp;
    /**
     * The level needed to mine the block.
     */
    private int minLevel;

    /**
     * The default constructor, used to create a new MiningBlock.
     * Checks if the given material is a block.
     * @param material The material.
     * @param xp The xp amount.
     * @param minLevel The level needed to mine the block.
     * @throws IllegalArgumentException If the material is not a block.
     */
    public MiningBlock(Material material, int xp, int minLevel) {
        if (material.isBlock()) {
            this.materials.add(material.name());
            this.xp = xp;
            this.minLevel = minLevel;
        } else {
            throw new IllegalArgumentException(material.name() + " is not a block type.");
        }
    }


    /**
     * The default constructor, used to create a new MiningBlock.
     * Checks if the given materials are blocks.
     * @param materials The materials.
     * @param xp The xp amount.
     * @param minLevel The level needed to mine the block.v
     * @throws IllegalArgumentException If the materials are not all blocks.
     */
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

    /**
     * @return All materials defined by this MiningBlock.
     */
    public ArrayList<Material> getMaterials() {
        ArrayList<Material> materials = new ArrayList<>();
        for (String material : this.materials) {
            materials.add(Material.valueOf(material));
        }
        return materials;
    }

    /**
     * @return The xp amount.
     */
    public int getXp() {
        return xp;
    }

    /**
     * @return The level needed to mine the block.
     */
    public int getMinLevel() {
        return minLevel;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setMinLevel(int minLevel) {
        if(minLevel > 0 && minLevel < MiningLevel.miningLevels.size()) {
            this.minLevel = minLevel;
        }
    }

    public void setMaterials(ArrayList<ItemStack> materials) {
        ArrayList<String> newMaterials = new ArrayList<>();
        for (ItemStack material : materials) {
            newMaterials.add(material.getType().name());
        }
        this.materials = newMaterials;
    }

    //Static
    /**
     * A list of all existing MiningBlocks.
     */
    public static ArrayList<MiningBlock> miningBlocks = new ArrayList<>();

    /**
     * A method to load all MiningBlocks registered in the file FileManager.BLOCKS, into the static ArrayList miningBlocks.
     * @throws IOException If the file FileManager.BLOCKS is not found.
     */
    public static void init() throws IOException {
        miningBlocks = Json.fromJson(FileManager.BLOCKS, new TypeToken<ArrayList<MiningBlock>>() {
        }.getType());
    }

    /**
     * Reloads all MiningBlocks from the file FileManager.blocks.
     * Basically the same as init().
     * @throws IOException If the file FileManager.BLOCKS is not found.
     */
    public static void reload() throws IOException {
        init();
    }

    /**
     * Saves all MiningBlocks from the static ArrayList miningBlocks into the file FileManager.blocks.
     * @throws IOException If the file could not be saved.
     */
    public static void save() throws IOException {
        if (miningBlocks != null) {
            FileManager.saveFile(FileManager.BLOCKS, miningBlocks);
        }
    }

    /**
     * Searches for a MiningBlock with the given material.
     * @param material The material.
     * @return The MiningBlock with the given material. Null if no MiningBlock with the given material is found.
     */
    public static MiningBlock getMiningBlock(Material material) {
        for (MiningBlock miningBlock : miningBlocks) {
            if (miningBlock.getMaterials().contains(material)) {
                return miningBlock;
            }
        }
        return null;
    }
}
