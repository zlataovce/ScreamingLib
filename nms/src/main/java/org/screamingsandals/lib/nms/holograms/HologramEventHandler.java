package org.screamingsandals.lib.nms.holograms;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class HologramEventHandler implements Listener {
    public static final int VISIBILITY_DISTANCE_SQUARED = 4096;
    private final HologramManager manager;
    private final Plugin plugin;

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (manager.getActiveHolograms().isEmpty()) {
            return;
        }

        List.copyOf(manager.getActiveHolograms()).forEach(hologram -> {
            if (hologram.isEmpty() || !hologram.hasViewers()) {
                manager.removeHologram(hologram);
                return;
            }

            try {
                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var loc = hologram.getLocation();

                if (viewers.contains(player)
                        && player.getWorld().equals(loc.getWorld())
                        && !event.getFrom().equals(loc.getWorld())) {
                    if (player.getLocation().distanceSquared(loc) < VISIBILITY_DISTANCE_SQUARED) {
                        hologram.update(player, hologram.getAllSpawnPackets(), false);
                    }
                }
            } catch (Throwable ignored) {
            }
        });
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (manager.getActiveHolograms().isEmpty()) {
            return;
        }

        List.copyOf(manager.getActiveHolograms()).forEach(hologram -> {
            if (hologram.isEmpty() || !hologram.hasViewers()) {
                manager.removeHologram(hologram);
                return;
            }

            try {
                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var location = hologram.getLocation();

                if (viewers.contains(player)
                        && event.getRespawnLocation().getWorld().equals(location.getWorld())) {
                    if (player.getLocation().distanceSquared(location) < VISIBILITY_DISTANCE_SQUARED) {
                        new BukkitRunnable() {
                            public void run() {
                                try {
                                    hologram.update(player, hologram.getAllSpawnPackets(), false);
                                } catch (Throwable ignored) {
                                }
                            }
                        }.runTaskLater(plugin, 20L);
                    }
                }
            } catch (Throwable ignored) {
            }
        });
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (manager.getActiveHolograms().isEmpty()) {
            return;
        }

        handleHologram(event);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (manager.getActiveHolograms().isEmpty()
                || !event.getFrom().getWorld().equals(event.getTo().getWorld())) {
            return;
        }

        handleHologram(event);
    }

    private void handleHologram(PlayerMoveEvent event) {
        List.copyOf(manager.getActiveHolograms()).forEach(hologram -> {
            if (hologram.isEmpty() || !hologram.hasViewers()) {
                manager.removeHologram(hologram);
                return;
            }

            try {
                final var player = event.getPlayer();
                final var viewers = hologram.getViewers();
                final var location = hologram.getLocation();

                if (viewers.contains(player) && player.getWorld().equals(location.getWorld())) {
                    if (event.getTo().distanceSquared(location) < VISIBILITY_DISTANCE_SQUARED
                            && event.getFrom().distanceSquared(location) >= VISIBILITY_DISTANCE_SQUARED) {
                        hologram.update(player, hologram.getAllSpawnPackets(), false);
                    } else if (event.getTo().distanceSquared(location) >= VISIBILITY_DISTANCE_SQUARED
                            && event.getFrom().distanceSquared(location) < VISIBILITY_DISTANCE_SQUARED) {
                        hologram.update(player, List.of(hologram.getFullDestroyPacket()), false);
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });
    }
}
