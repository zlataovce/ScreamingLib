package org.screamingsandals.lib.core.listener;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.screamingsandals.lib.core.wrapper.player.PlayerWrapperService;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class BukkitPlayerListener implements Listener {
    private final PlayerWrapperService service;

    @Inject
    public BukkitPlayerListener(PlayerWrapperService service, PluginWrapper wrapper) {
        this.service = service;
        wrapper.registerListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        service.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent event) {
        service.remove(event.getPlayer().getUniqueId());
    }
}
