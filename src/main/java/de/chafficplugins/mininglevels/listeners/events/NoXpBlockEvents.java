package de.chafficplugins.mininglevels.listeners.events;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendDebug;

public class NoXpBlockEvents implements Listener {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    private static final ArrayList<PlayerWithTNT> playersWithTNT = new ArrayList<>();
    static final ArrayList<Block> noXpBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if(!plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS)) {
            sendDebug(event.getPlayer(), "Block placed: " + event.getBlock().getType());
            final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
            if(block != null) {
                noXpBlocks.add(event.getBlock());
                sendDebug(event.getPlayer(), "Player placed block registered.");
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

    @EventHandler
    public void onTNTFired(final PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if(!plugin.getConfigBoolean(DESTROY_MINING_BLOCKS_ON_EXPLODE) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block != null) {
            sendDebug(event.getPlayer(), "Block clicked: " + block.getType());
            if(block.getType().equals(Material.TNT) || block.getType().equals(Material.TNT_MINECART)) {
                OfflinePlayer player = event.getPlayer();
                ItemStack item = event.getItem();

                if(item != null && item.getType().equals(Material.FLINT_AND_STEEL)) {
                    playersWithTNT.add(new PlayerWithTNT(player, block));
                    sendDebug(event.getPlayer(), "Player with TNT registered.");
                }
            }
        }
    }

    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent event) {
        Block b = event.getLocation().getBlock();

        if(!plugin.getConfigBoolean(DESTROY_MINING_BLOCKS_ON_EXPLODE)) {
            PlayerWithTNT playerWithTNT = getPlayerWithTNT(b);
            if(playerWithTNT != null) {
                sendDebug(playerWithTNT.player.getPlayer(), "Block exploded: " + b.getType());
                ArrayList<Block> blocksToRemove = new ArrayList<>();
                for(final Block block : event.blockList()) {
                    MiningBlock miningBlock = MiningBlock.getMiningBlock(block.getType());
                    MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(playerWithTNT.getPlayer().getUniqueId());
                    if(miningBlock != null && miningPlayer != null) {
                        if(miningPlayer.getLevel().getOrdinal() < miningBlock.getMinLevel()) {
                            blocksToRemove.add(block);
                        }
                    }
                    sendDebug(playerWithTNT.player.getPlayer(), "Higher level block destruction disabled.");
                }
                event.blockList().removeAll(blocksToRemove);
            }
        }
    }

    private static class PlayerWithTNT {
        private final OfflinePlayer player;
        private final Block tnt;

        public PlayerWithTNT(OfflinePlayer player, Block tnt) {
            this.player = player;
            this.tnt = tnt;
        }

        public OfflinePlayer getPlayer() {
            return player;
        }

        public Block getTnt() {
            return tnt;
        }
    }

    private PlayerWithTNT getPlayerWithTNT(Block block) {
        for(final PlayerWithTNT playerWithTNT : playersWithTNT) {
            if(playerWithTNT.getTnt().equals(block)) {
                return playerWithTNT;
            }
        }
        return null;
    }
}
