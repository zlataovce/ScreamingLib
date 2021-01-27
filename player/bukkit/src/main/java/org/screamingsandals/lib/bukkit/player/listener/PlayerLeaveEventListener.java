package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerQuitEvent;
import org.screamingsandals.lib.bukkit.event.AbstractEventListener;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerLeaveEvent;

public class PlayerLeaveEventListener extends AbstractEventListener<PlayerQuitEvent> {

    @Override
    protected void onFire(PlayerQuitEvent event, org.screamingsandals.lib.event.EventPriority eventPriority) {
        final var toFire = new SPlayerLeaveEvent(PlayerMapper.wrapPlayer(event.getPlayer()));
        EventManager.getDefaultEventManager().fireEvent(toFire, eventPriority);
    }
}
