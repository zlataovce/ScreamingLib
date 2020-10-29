package org.screamingsandals.lib.core.wrapper.player;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.core.wrapper.sender.AbstractSender;

import java.util.UUID;

class VelocityPlayerWrapper extends AbstractSender<Player> implements PlayerWrapper<Player> {

    VelocityPlayerWrapper(Player instance) {
        super(instance, instance);
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
