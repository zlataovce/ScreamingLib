package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerInteractEntityEvent;

public class PlayerInteractEntityEventListener extends AbstractBukkitEventHandlerFactory<PlayerInteractEntityEvent, SPlayerInteractEntityEvent> {

    public PlayerInteractEntityEventListener(Plugin plugin) {
        super(PlayerInteractEntityEvent.class, SPlayerInteractEntityEvent.class, plugin);
    }

    @Override
    protected SPlayerInteractEntityEvent wrapEvent(PlayerInteractEntityEvent event, EventPriority priority) {
        return new SPlayerInteractEntityEvent(
                PlayerMapper.wrapPlayer(event.getPlayer()),
                EntityMapper.wrapEntity(event.getRightClicked()).orElseThrow(),
                EquipmentSlotMapping.resolve(event.getHand()).orElseThrow()
        );
    }
}
