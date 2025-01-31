package org.screamingsandals.lib.visuals.impl;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.visuals.Visual;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractVisual<T extends Visual<T>> implements Visual<T> {
    protected final List<PlayerWrapper> viewers = new CopyOnWriteArrayList<>();
    protected final UUID uuid;
    protected boolean visible = false;

    public AbstractVisual(UUID uuid) {
        this.uuid = uuid;
    }

    protected abstract void update0();

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Collection<PlayerWrapper> getViewers() {
        return List.copyOf(viewers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T addViewer(PlayerWrapper viewer) {
        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
            onViewerAdded(viewer, true);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T removeViewer(PlayerWrapper viewer) {
        if (viewers.contains(viewer)) {
            viewers.remove(viewer);
            onViewerRemoved(viewer, false);
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T clearViewers() {
        hide();
        viewers.clear();
        return (T) this;
    }

    @Override
    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    @Override
    public boolean isShown() {
        return visible;
    }
}
