package org.screamingsandals.lib.bukkit.listener;

import org.bukkit.event.block.*;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.event.block.SBlockFadeEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.block.state.BlockStateMapper;

public class BlockFadeEventListener extends AbstractBukkitEventHandlerFactory<BlockFadeEvent, SBlockFadeEvent> {

    public BlockFadeEventListener(Plugin plugin) {
        super(BlockFadeEvent.class, SBlockFadeEvent.class, plugin);
    }

    @Override
    protected SBlockFadeEvent wrapEvent(BlockFadeEvent event, EventPriority priority) {
        return new SBlockFadeEvent(
                ImmutableObjectLink.of(() -> BlockMapper.wrapBlock(event.getBlock())),
                ImmutableObjectLink.of(() -> BlockStateMapper.wrapBlockState(event.getNewState()).orElseThrow())
        );
    }
}
