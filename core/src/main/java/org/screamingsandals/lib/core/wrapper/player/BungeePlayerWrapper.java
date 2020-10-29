package org.screamingsandals.lib.core.wrapper.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.screamingsandals.lib.core.wrapper.sender.AbstractSender;

import java.util.UUID;

class BungeePlayerWrapper extends AbstractSender<ProxiedPlayer> implements PlayerWrapper<ProxiedPlayer> {

    BungeePlayerWrapper(ProxiedPlayer instance, Audience audience) {
        super(instance, audience);
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public void sendMessage(Component message) {
        audience.sendMessage(Identity.identity(getUuid()), message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return instance.hasPermission(permission);
    }

    @Override
    public String getAddress() {
        final var address = instance.getAddress();
        if (address == null) {
            return "UNKNOWN";
        }

        return address.getAddress().getHostAddress();
    }

    @Override
    public void kick(Component reason) {
        instance.disconnect(LegacyComponentSerializer.legacyAmpersand().serialize(reason));
    }
}
