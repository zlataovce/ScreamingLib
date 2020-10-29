package org.screamingsandals.lib.core.wrapper.player;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.UUID;

/**
 * Created for hoz.network, ported to ScreamingSandals
 *
 * @param <T> Type of Player
 */
public interface PlayerWrapper<T> extends SenderWrapper<T> {

    UUID getUuid();

    String getAddress();

    void kick(Component reason);

    static <K> PlayerWrapper<K> of(K instance) {
        return PlayerWrapperService.get( instance);
    }
}
