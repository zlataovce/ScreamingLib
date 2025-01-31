package org.screamingsandals.lib.visuals;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Collection;
import java.util.UUID;

/**
 * Represents Visual that can have title and viewers.
 */
public interface Visual<T> {

    /**
     * Each visual can have its UUID.
     *
     * @return UUID of this visual.
     */
    UUID getUuid();

    /**
     * @return viewers that are currently looking and this visual.
     */
    Collection<PlayerWrapper> getViewers();

    T update();

    T show();

    T hide();

    T addViewer(PlayerWrapper viewer);

    T removeViewer(PlayerWrapper viewer);

    T clearViewers();

    T title(Component title);

    T title(ComponentLike title);

    /**
     * Checks if this Visual has any viewers.
     *
     * @return true if yes, duh.
     */
    boolean hasViewers();

    boolean isShown();

    void destroy();

    void onViewerAdded(PlayerWrapper viewer, boolean checkDistance);

    void onViewerRemoved(PlayerWrapper viewer, boolean checkDistance);
}
