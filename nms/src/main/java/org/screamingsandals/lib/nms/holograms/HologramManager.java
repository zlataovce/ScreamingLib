package org.screamingsandals.lib.nms.holograms;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.nms.network.inbound.AutoPacketInboundListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.PacketPlayInUseEntity;
import static org.screamingsandals.lib.reflect.SReflect.getField;

@Data
public class HologramManager {
    private final List<Hologram> activeHolograms = new LinkedList<>();
    private final Plugin plugin;
    private final AutoPacketInboundListener packetInboundListener;

    public HologramManager(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(
                new HologramEventHandler(this, plugin), plugin);

        packetInboundListener = new AutoPacketInboundListener(plugin) {
            @Override
            protected Object handle(Player sender, Object packet) {
                if (PacketPlayInUseEntity.isInstance(packet)) {
                    final var field = (int) getField(PacketPlayInUseEntity, "a,field_149567_a", packet);

                    activeHolograms.forEach(hologram -> hologram.handleTouch(sender, field));
                }
                return packet;
            }
        };
    }

    public void destroy() {
        activeHolograms.forEach(this::removeHologram);
    }

    public HologramBuilder newHologram() {
        return new HologramBuilder(this);
    }

    public void registerHologram(Hologram hologram) {
        activeHolograms.add(hologram);
    }

    public void removeHologram(Hologram hologram) {
        hologram.destroy();
        activeHolograms.remove(hologram);
    }

    @RequiredArgsConstructor
    public static class HologramBuilder {
        private final HologramManager manager;
        private final List<Player> viewers = new LinkedList<>();
        private final List<String> lines = new LinkedList<>();
        private final List<TouchCallback> callbacks = new LinkedList<>();
        private Location location;

        public HologramBuilder lines(String... lines) {
            this.lines.addAll(Arrays.asList(lines));
            return this;
        }

        public HologramBuilder viewers(Player... viewers) {
            this.viewers.addAll(Arrays.asList(viewers));
            return this;
        }

        public HologramBuilder callbacks(TouchCallback... callbacks) {
            this.callbacks.addAll(Arrays.asList(callbacks));
            return this;
        }

        public HologramBuilder lines(List<String> lines) {
            this.lines.addAll(lines);
            return this;
        }

        public HologramBuilder viewers(List<Player> viewers) {
            this.viewers.addAll(viewers);
            return this;
        }

        public HologramBuilder callbacks(List<TouchCallback> callbacks) {
            this.callbacks.addAll(callbacks);
            return this;
        }

        public HologramBuilder location(Location location) {
            this.location = location;
            return this;
        }

        public Hologram build() {
            if (location == null) {
                throw new UnsupportedOperationException("Location of Hologram is undefined!");
            }

            final var hologram = new Hologram(viewers, lines, callbacks, location);
            manager.registerHologram(hologram);

            return hologram;
        }
    }
}
