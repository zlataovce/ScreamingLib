package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.event.player.SPlayerToggleSneakEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

public class PlayerToggleSneakEventListener extends AbstractBukkitEventHandlerFactory<PlayerToggleSneakEvent, SPlayerToggleSneakEvent> {

    public PlayerToggleSneakEventListener(Plugin plugin) {
        super(PlayerToggleSneakEvent.class, SPlayerToggleSneakEvent.class, plugin);
    }

    @Override
    protected SPlayerToggleSneakEvent wrapEvent(PlayerToggleSneakEvent event, EventPriority priority) {
        return new SPlayerToggleSneakEvent(
                ImmutableObjectLink.of(() -> PlayerMapper.wrapPlayer(event.getPlayer())),
                ImmutableObjectLink.of(event::isSneaking)
        );
    }
}
