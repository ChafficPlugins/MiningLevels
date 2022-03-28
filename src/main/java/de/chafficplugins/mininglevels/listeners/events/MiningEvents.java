package de.chafficplugins.mininglevels.listeners.events;

import de.chafficplugins.mininglevels.MiningLevels;
import de.chafficplugins.mininglevels.api.MiningBlock;
import de.chafficplugins.mininglevels.api.MiningLevel;
import de.chafficplugins.mininglevels.api.MiningPlayer;
import de.chafficplugins.mininglevels.utils.MathUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

import static de.chafficplugins.mininglevels.utils.ConfigStrings.LEVEL_WITH_PLAYER_PLACED_BLOCKS;

public class MiningEvents implements Listener {
    private static final MiningLevels plugin = MiningLevels.getPlugin(MiningLevels.class);
    private static final ArrayList<Block> playerPlacedBlock = new ArrayList<>();

    @EventHandler
    public void onBlockDamage(final BlockDamageEvent event) {
        final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
        if(block != null) {
            final MiningPlayer miningPlayer = MiningPlayer.getMiningPlayer(event.getPlayer().getUniqueId());
            if(miningPlayer != null) {
                if(miningPlayer.getLevel().getOrdinal() < block.getMinLevel()) {
                    event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Level " + block.getMinLevel() + " needed!"));
                    event.setCancelled(true);
                } else {
                    if(miningPlayer.getLevel().getHasteLevel() > 0) {
                        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 5*20, miningPlayer.getLevel().getHasteLevel()));
                    }
                    if(MathUtils.randomDouble(0,100) < miningPlayer.getLevel().getInstantBreakProbability()) {
                        event.setInstaBreak(true); //Insta break
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
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
                    event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Level " + level.getName() + " needed!"));
                } else {
                    //check if the block was placed by a player
                    if(!plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS) || !playerPlacedBlock.contains(event.getBlock())) {
                        miningPlayer.alterXp(block.getXp());
                        MiningLevel level = miningPlayer.getLevel();
                        if(MathUtils.randomDouble(0,100) < level.getExtraOreProbability()) {
                            Block actualBlock = event.getBlock();
                            for (int i = 0; i < (int) MathUtils.randomDouble(1, level.getMaxExtraOre()); i++) {
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
        if(plugin.getConfigBoolean(LEVEL_WITH_PLAYER_PLACED_BLOCKS)) {
            final MiningBlock block = MiningBlock.getMiningBlock(event.getBlock().getType());
            if(block != null) {
                playerPlacedBlock.add(event.getBlock());
            }
        }
    }
}
