package org.screamingsandals.lib.core.listener;

import com.google.inject.Inject;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.screamingsandals.lib.core.wrapper.player.PlayerWrapperService;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class BungeePlayerListener implements Listener {
    private final PlayerWrapperService service;

    @Inject
    public BungeePlayerListener(PlayerWrapperService service, PluginWrapper wrapper) {
        this.service = service;
        wrapper.registerListener(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent event) {
        service.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerDisconnectEvent event) {
        service.remove(event.getPlayer().getUniqueId());
    }
}
