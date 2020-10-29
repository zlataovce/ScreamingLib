package org.screamingsandals.lib.core.listener;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import org.screamingsandals.lib.core.wrapper.player.PlayerWrapperService;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class VelocityPlayerListener {
    private final PlayerWrapperService service;

    @Inject
    public VelocityPlayerListener(PlayerWrapperService service, PluginWrapper wrapper) {
        this.service = service;
        wrapper.registerListener(this);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onJoin(PostLoginEvent event) {
        service.register(event.getPlayer());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLeave(DisconnectEvent event) {
        service.remove(event.getPlayer().getUniqueId());
    }
}
