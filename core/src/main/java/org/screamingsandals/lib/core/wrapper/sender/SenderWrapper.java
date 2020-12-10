package org.screamingsandals.lib.core.wrapper.sender;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.core.wrapper.player.PlayerWrapper;

public interface SenderWrapper<T> {

    static SenderWrapper<org.bukkit.command.CommandSender> of(org.bukkit.command.CommandSender commandSender) {
        return SenderWrapperService.wrap(commandSender);
    }

    static SenderWrapper<net.md_5.bungee.api.CommandSender> of(net.md_5.bungee.api.CommandSender commandSender) {
        return SenderWrapperService.wrap(commandSender);
    }

    static SenderWrapper<com.velocitypowered.api.command.CommandSource> of(com.velocitypowered.api.command.CommandSource commandSource) {
        return new AbstractSender<>(commandSource, commandSource) {
        };
    }

    /**
     * Mostly a console sender.
     *
     * @return Instance of this sender.
     */
    T getInstance();

    /**
     * If the instance is console, returns just "CONSOLE" :)
     *
     * @return Name of the instance.
     */
    String getName();

    /**
     * Audience of this sender.
     *
     * @return an {@link Audience}
     */
    Audience getAudience();

    /**
     * Sends message with {@link Identity}.nil()
     * If the instance is Player, the identity is set from {@link java.util.UUID}
     *
     * @param message message to send
     */
    void sendMessage(Component message);

    /**
     * @param identity identity to use
     * @param message  message to send
     */
    void sendMessage(Identity identity, Component message);

    boolean hasPermission(String permission);

    /**
     * @return Player wrapper of this sender.
     */
    default <K> PlayerWrapper<K> getPlayer() {
        if (isPlayer()) {
            return (PlayerWrapper<K>) this;
        }

        return null;
    }

    default boolean isPlayer() {
        return this instanceof PlayerWrapper;
    }
}
