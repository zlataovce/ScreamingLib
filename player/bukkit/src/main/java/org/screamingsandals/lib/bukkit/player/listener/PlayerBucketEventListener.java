package org.screamingsandals.lib.bukkit.player.listener;

import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.event.AbstractBukkitEventHandlerFactory;
import org.screamingsandals.lib.event.EventPriority;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.event.SPlayerBucketEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockMapper;

public class PlayerBucketEventListener extends AbstractBukkitEventHandlerFactory<PlayerBucketEvent, SPlayerBucketEvent> {

    public PlayerBucketEventListener(Plugin plugin) {
        super(PlayerBucketEvent.class, SPlayerBucketEvent.class, plugin);
    }

    @Override
    protected SPlayerBucketEvent wrapEvent(PlayerBucketEvent event, EventPriority priority) {
            return new SPlayerBucketEvent(
                    PlayerMapper.wrapPlayer(event.getPlayer()),
                    BlockMapper.wrapBlock(event.getBlock()),
                    BlockMapper.wrapBlock(event.getBlockClicked()),
                    BlockFace.valueOf(event.getBlockFace().name().toUpperCase()),
                    MaterialMapping.resolve(event.getBucket()).orElseThrow(),
                    ItemFactory.build(event.getItemStack()).orElse(null)
                    );
    }
}
