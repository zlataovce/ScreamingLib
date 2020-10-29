package org.screamingsandals.lib.core.wrapper.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.core.wrapper.sender.AbstractSender;

import java.util.UUID;

class BukkitPlayerWrapper extends AbstractSender<Player> implements PlayerWrapper<Player> {

    BukkitPlayerWrapper(Player instance, Audience audience) {
        super(instance, audience);
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public void sendMessage(Component message) {
        sendMessage(Identity.identity(getUuid()), message);
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
        instance.kickPlayer(LegacyComponentSerializer.legacyAmpersand().serialize(reason));
    }
}
