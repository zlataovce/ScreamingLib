package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.BlockHolder;

// TODO: this event is actually redundant
@EqualsAndHashCode(callSuper = true)
public class SEntityBreakDoorEvent extends SEntityChangeBlockEvent {

    public SEntityBreakDoorEvent(ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<BlockHolder> block, ImmutableObjectLink<BlockDataHolder> to) {
        super(entity, block, to);
    }

}
