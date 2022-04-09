package de.chafficplugins.mininglevels.listeners.events;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.*;

public class MiningEvents implements Listener {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    private static final ArrayList<Block> noXpBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockDamage(final BlockDamageEvent event) {
        final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
        if(block != null) {
            final MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(event.getPlayer().getUniqueId());
            if(miningPlayer != null) {
                if(miningPlayer.getLevel().getOrdinal() < block.getMinLevel()) {
                    MiningLevel level = MiningLevel.get(block.getMinLevel());
                    if(level == null) return;
                    miningPlayer.showMessage(LEVEL_NEEDED, ChatColor.RED, level.getName());
                    event.setCancelled(true);
                } else if(isMiningItem(event.getItemInHand().getType())) {
                    //check if the block was placed by a player
                    if(plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS) || !noXpBlocks.contains(event.getBlock())) {
                        if(miningPlayer.getLevel().getHasteLevel() > 0) {
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 5*20, miningPlayer.getLevel().getHasteLevel()));
                        }
                        if(MathUtils.randomDouble(0,100) < miningPlayer.getLevel().getInstantBreakProbability()) {
                            event.setInstaBreak(true); //Insta break
                        }
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    private boolean isMiningItem(final Material material) {
        return plugin.getConfig().getStringList(MINING_ITEMS).contains(material.name());
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
        if(block != null) {
            final MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(event.getPlayer().getUniqueId());
            if(miningPlayer != null) {
                if(miningPlayer.getLevel().getOrdinal() < block.getMinLevel()) {
                    event.setCancelled(true);
                    MiningLevel level = MiningLevel.get(block.getMinLevel());
                    if(level == null) return;
                    miningPlayer.showMessage(LEVEL_NEEDED, ChatColor.RED, level.getName());
                } else {
                    //check if the block was placed by a player
                    if(plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS) || !noXpBlocks.contains(event.getBlock())) {
                        miningPlayer.alterXp(block.getXp());
                        MiningLevel level = miningPlayer.getLevel();
                        if(event.getPlayer().getItemInUse() != null && isMiningItem(event.getPlayer().getItemInUse().getType()) && MathUtils.randomDouble(0,100) < level.getExtraOreProbability()) {
                            Block actualBlock = event.getBlock();
                            int amount = (int) MathUtils.randomDouble(1, level.getMaxExtraOre());
                            for (int i = 0; i < amount; i++) {
                                event.getPlayer().getWorld().dropItemNaturally(actualBlock.getLocation(), actualBlock.getDrops().iterator().next());
                            }
                        }
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

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
        if(!plugin.getConfigBoolean(LEVEL_WITH_GENERATED_BLOCKS)) {;
            final MiningBlock block = MiningBlock.getMiningBlock(event.getNewState().getType());
            if(block != null) {
                noXpBlocks.add(event.getBlock());
            }
        }
    }
}
