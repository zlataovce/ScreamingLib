package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.player.SPlayerPickupItemEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@SuppressWarnings("deprecation") //legacy bukkit versions
public class LegacyPlayerPickupItemListener extends AbstractBukkitEventHandlerFactory<PlayerPickupItemEvent, SPlayerPickupItemEvent> {

    public LegacyPlayerPickupItemListener(Plugin plugin) {
        super(PlayerPickupItemEvent.class, SPlayerPickupItemEvent.class, plugin);
    }

    @Override
    protected SPlayerPickupItemEvent wrapEvent(PlayerPickupItemEvent event, EventPriority priority) {
        return new SPlayerPickupItemEvent(
                ImmutableObjectLink.of(() -> EntityMapper.wrapEntity(event.getPlayer()).orElseThrow()),
                ImmutableObjectLink.of(() -> EntityMapper.<EntityItem>wrapEntity(event.getItem()).orElseThrow()),
                ImmutableObjectLink.of(event::getRemaining)
        );
    }
}
