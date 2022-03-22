package de.chafficplugins.mininglevels.listeners.events;

import de.chafficplugins.mininglevels.api.MiningPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class ServerEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        if(!MiningPlayer.exists(event.getPlayer().getUniqueId())) {
            new MiningPlayer(event.getPlayer().getUniqueId(), 0, 0);
            MiningPlayer.save();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) throws IOException {
        if(!MiningPlayer.exists(event.getPlayer().getUniqueId())) {
            new MiningPlayer(event.getPlayer().getUniqueId(), 0, 0);
        }
        MiningPlayer.save();
    }
}
