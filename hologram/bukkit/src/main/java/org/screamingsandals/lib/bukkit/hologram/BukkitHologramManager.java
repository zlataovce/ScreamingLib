package org.screamingsandals.lib.bukkit.hologram;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.hologram.event.HologramTouchEvent;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.initializer.AbstractTaskInitializer;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.visuals.VisualsTouchListener;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import java.util.UUID;

@Slf4j
@Service(dependsOn = {
        EventManager.class,
        PlayerMapper.class,
        LocationMapper.class,
        AbstractTaskInitializer.class,
        ItemFactory.class
})
public class BukkitHologramManager extends HologramManager {

    @Deprecated //INTERNAL USE ONLY!
    public static BukkitHologramManager init(Plugin plugin, Controllable controllable) {
        final var bukkitHologramManager =  new BukkitHologramManager(plugin, controllable);
        HologramManager.init(() -> bukkitHologramManager);
        return bukkitHologramManager;
    }

    protected BukkitHologramManager(Plugin plugin, Controllable controllable) {
        super(controllable);
        controllable.postEnable(() -> new VisualsTouchListener<>(BukkitHologramManager.this, plugin));
    }

    @Override
    public void fireVisualTouchEvent(PlayerWrapper sender, Hologram visual, Object packet) {
        Tasker.build(() -> EventManager.fire(new HologramTouchEvent(PlayerMapper.wrapPlayer(sender), visual))).afterOneTick().start();
    }

    @Override
    public Hologram createVisual(UUID uuid, LocationHolder holder, boolean touchable) {
        return new BukkitHologram(uuid, holder, touchable);
    }
}
