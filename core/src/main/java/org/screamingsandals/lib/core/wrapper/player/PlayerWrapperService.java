package org.screamingsandals.lib.core.wrapper.player;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.platform.AudienceProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.screamingsandals.lib.core.reflect.SReflect;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlayerWrapperService {
    private final Logger log = LoggerFactory.getLogger(PlayerWrapperService.class);
    private final Map<UUID, PlayerWrapper<?>> cachedWraps = new HashMap<>();
    private final PluginWrapper pluginWrapper;

    @Inject(optional = true)
    private AudienceProvider audienceProvider;

    private static PlayerWrapperService instance;

    @Inject
    public PlayerWrapperService(PluginWrapper pluginWrapper) {
        this.pluginWrapper = pluginWrapper;

        instance = this;
    }

    public static <T> PlayerWrapper<T> get(T player) {
        return instance.getWrapped(getUuid(player), player);
    }

    @SuppressWarnings("unchecked")
    public <T> PlayerWrapper<T> getWrapped(UUID uuid, T instance) {
        if (cachedWraps.containsKey(uuid)) {
            return (PlayerWrapper<T>) cachedWraps.get(uuid);
        }

        return (PlayerWrapper<T>) wrap(uuid, instance);
    }

    public Optional<PlayerWrapper<?>> getById(UUID uuid) {
        return Optional.ofNullable(cachedWraps.get(uuid));
    }

    public <T> void register(T instance) {
        final var uuid = getUuid(instance);
        cachedWraps.put(uuid, wrap(uuid, instance));

        log.debug("Registering player {}", uuid.toString());
    }

    public void remove(UUID uuid) {
        cachedWraps.remove(uuid);
        log.debug("Removing player {}", uuid.toString());
    }

    private PlayerWrapper<?> wrap(UUID uuid, Object instance) {
        switch (pluginWrapper.getType()) {
            case VELOCITY:
                return new VelocityPlayerWrapper((Player) instance);
            case BUNGEE:
                return new BungeePlayerWrapper((ProxiedPlayer) instance, audienceProvider.player(uuid));
            case BUKKIT:
                return new BukkitPlayerWrapper((org.bukkit.entity.Player) instance, audienceProvider.player(uuid));
            default:
                throw new UnsupportedOperationException("This type of player is not supported!");
        }
    }

    private static UUID getUuid(Object instance) {
        return (UUID) SReflect.fastInvoke(instance, "getUniqueId");
    }
}
