package de.chafficplugins.mininglevels.listeners.events;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.LEVEL_WITH_GENERATED_BLOCKS;
import static de.chafficplugins.mininglevels.utils.ConfigStrings.LEVEL_WITH_PLAYER_PLACED_BLOCKS;

public class NoXpBlockEvents implements Listener {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    static final ArrayList<Block> noXpBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if(!plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS)) {
            final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
            if(block != null) {
                noXpBlocks.add(event.getBlock());
            }
        }
    }

    @EventHandler
    public void onBlockGenerated(final BlockFormEvent event) {
        if(!plugin.getConfigBoolean(LEVEL_WITH_GENERATED_BLOCKS)) {
            final MiningBlock block = MiningBlock.getMiningBlock(event.getNewState().getType());
            if(block != null) {
                noXpBlocks.add(event.getBlock());
            }
        }
    }
}
