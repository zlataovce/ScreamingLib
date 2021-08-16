package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.entity.pose.EntityPoseHolder;
import org.screamingsandals.lib.event.entity.SEntityPoseChangeEvent;
import org.screamingsandals.lib.event.EventPriority;

public class EntityPoseChangeEventListener extends AbstractBukkitEventHandlerFactory<EntityPoseChangeEvent, SEntityPoseChangeEvent> {

    public EntityPoseChangeEventListener(Plugin plugin) {
        super(EntityPoseChangeEvent.class, SEntityPoseChangeEvent.class, plugin);
    }

    @Override
    protected SEntityPoseChangeEvent wrapEvent(EntityPoseChangeEvent event, EventPriority priority) {
        return new SEntityPoseChangeEvent(
                EntityMapper.wrapEntity(event.getEntity()).orElseThrow(),
                EntityPoseHolder.of(event.getPose())
        );
    }
}
