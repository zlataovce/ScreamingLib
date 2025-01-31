package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.event.block.SBlockExplodeEvent;

public class BlockExplodeEventListener extends AbstractBukkitEventHandlerFactory<BlockExplodeEvent, SBlockExplodeEvent> {

    public BlockExplodeEventListener(Plugin plugin) {
        super(BlockExplodeEvent.class, SBlockExplodeEvent.class, plugin);
    }

    @Override
    protected SBlockExplodeEvent wrapEvent(BlockExplodeEvent event, EventPriority priority) {
        return new SBlockExplodeEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock),
                ObjectLink.of(event::getYield, event::setYield)
        );
    }
}
