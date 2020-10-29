package org.screamingsandals.lib.core.wrapper.sender;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityPlayerWrapper extends AbstractSender<Player> implements PlayerWrapper<Player> {

    VelocityPlayerWrapper(Player instance) {
        super(instance, instance);
    }

    public static VelocityPlayerWrapper of(Player instance) {
        return new VelocityPlayerWrapper(instance);
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public String getAddress() {
        return instance.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public void kick(Component reason) {
        instance.disconnect(reason);
    }
}
