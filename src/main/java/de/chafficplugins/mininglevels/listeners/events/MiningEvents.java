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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static de.chafficplugins.mininglevels.listeners.events.NoXpBlockEvents.noXpBlocks;
import static de.chafficplugins.mininglevels.utils.ConfigStrings.LEVEL_NEEDED;
import static de.chafficplugins.mininglevels.utils.ConfigStrings.MINING_ITEMS;
import static de.chafficplugins.mininglevels.utils.SenderUtils.sendDebug;

public class MiningEvents implements Listener {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);

    @EventHandler
    public void onBlockDamage(final BlockDamageEvent event) {
        final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
        sendDebug(event.getPlayer(), "BlockDamageEvent: " + event.getBlock().getType().name());
        if(block != null) {
            final MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(event.getPlayer().getUniqueId());
            if(miningPlayer != null) {
                if (miningPlayer.getLevel().getOrdinal() < block.getMinLevel()) {
                    sendDebug(event.getPlayer(), "Level too low.");
                    MiningLevel level = MiningLevel.get(block.getMinLevel());
                    if (level == null) return;
                    miningPlayer.showMessage(LEVEL_NEEDED, ChatColor.RED, level.getName());
                    event.setCancelled(true);
                } else {
                    if (isMiningItem(event.getItemInHand().getType())) {
                        if (miningPlayer.getLevel().getHasteLevel() > 0) {
                            sendDebug(event.getPlayer(), "Haste level: " + miningPlayer.getLevel().getHasteLevel());
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 5 * 20, miningPlayer.getLevel().getHasteLevel()));
                        }
                        if (MathUtils.randomDouble(0, 100) < miningPlayer.getLevel().getInstantBreakProbability()) {
                            sendDebug(event.getPlayer(), "Instant break.");
                            event.setInstaBreak(true); //Insta break
                        }
                    } else {
                        sendDebug(event.getPlayer(), "The held item is not a mining item.");
                    }
                }
            } else {
                sendDebug(event.getPlayer(), "Error: Player is not registered to the plugin!");
                event.setCancelled(true);
            }
        } else {
            sendDebug(event.getPlayer(), "Not a mining block.");
        }
    }

    private boolean isMiningItem(final Material material) {
        return plugin.getConfig().getStringList(MINING_ITEMS).contains(material.name());
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
        sendDebug(event.getPlayer(), "BlockBreakEvent: " + event.getBlock().getType().name());
        if(block != null) {
            final MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(event.getPlayer().getUniqueId());
            if(miningPlayer != null) {
                if(miningPlayer.getLevel().getOrdinal() < block.getMinLevel()) {
                    sendDebug(event.getPlayer(), "Level too low.");
                    event.setCancelled(true);
                    MiningLevel level = MiningLevel.get(block.getMinLevel());
                    if(level == null) return;
                    miningPlayer.showMessage(LEVEL_NEEDED, ChatColor.RED, level.getName());
                } else {
                    //check if the block was placed by a player
                    if (noXpBlocks.contains(event.getBlock())) {
                        sendDebug(event.getPlayer(), "Config options disallow block to drop xp.");
                        return;
                    }

                    miningPlayer.alterXp(block.getXp());
                    MiningLevel level = miningPlayer.getLevel();
                    if (event.getPlayer().getItemInUse() == null || !isMiningItem(event.getPlayer().getItemInUse().getType())) {
                        sendDebug(event.getPlayer(), "The held item is not a mining item.");
                        return;
                    }

                    if (MathUtils.randomDouble(0, 100) < level.getExtraOreProbability()) {
                        Block actualBlock = event.getBlock();
                        int amount = (int) MathUtils.randomDouble(1, level.getMaxExtraOre());
                        for (int i = 0; i < amount; i++) {
                            event.getPlayer().getWorld().dropItemNaturally(actualBlock.getLocation(), actualBlock.getDrops().iterator().next());
                        }
                        sendDebug(event.getPlayer(), "Dropped " + amount + " extra ores.");
                    }
                }
            } else {
                sendDebug(event.getPlayer(), "Error: Player is not registered to the plugin!");
                event.setCancelled(true);
            }
        } else {
            sendDebug(event.getPlayer(), "Not a mining block.");
        }
    }
}
