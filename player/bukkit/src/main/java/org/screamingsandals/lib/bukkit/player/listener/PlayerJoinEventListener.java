package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.bukkit.event.AbstractEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;

public class PlayerJoinEventListener extends AbstractEventListener<PlayerJoinEvent> {

    @Override
    protected void onFire(PlayerJoinEvent event, EventPriority eventPriority) {
        final var toFire = new org.screamingsandals.lib.player.event.PlayerJoinEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
        EventManager.getDefaultEventManager().fireEvent(toFire, eventPriority);
    }
}
