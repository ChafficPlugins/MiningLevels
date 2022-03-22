package de.chafficplugins.mininglevels.api;

import com.google.gson.Gson;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

class MiningBlockTest {

    @Test
    public void testSave() throws IOException {
        MiningBlock.miningBlocks.add(new MiningBlock(Material.STONE, 1, 0));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_REDSTONE_ORE, Material.REDSTONE_ORE}, 1, 0));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_LAPIS_ORE, Material.LAPIS_ORE}, 1, 3));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_COAL_ORE, Material.COAL_ORE}, 10, 0));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_GOLD_ORE, Material.GOLD_ORE}, 100, 2));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD_ORE}, 10, 3));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_COPPER_ORE, Material.COPPER_ORE}, 5, 2));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_IRON_ORE, Material.IRON_ORE}, 50, 1));
        MiningBlock.miningBlocks.add(new MiningBlock(new Material[]{Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND_ORE}, 1000, 5));

        String json = new Gson().toJson(MiningBlock.miningBlocks);
        System.out.println(json);

        //save to file
        FileWriter writer = new FileWriter("src/test/resources/miningBlocks.json");
        writer.write(json);
    }

}