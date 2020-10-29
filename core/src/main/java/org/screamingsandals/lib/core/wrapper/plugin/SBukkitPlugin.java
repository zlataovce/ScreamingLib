package org.screamingsandals.lib.core.wrapper.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.core.ScreamingModule;
import org.screamingsandals.lib.core.wrapper.sender.PlayerWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public abstract class SBukkitPlugin extends JavaPlugin implements PluginWrapper {

    public abstract void enable(Injector injector);

    public abstract void disable();

    public abstract void load();

    @Override
    public void onEnable() {
        final var injector = Guice.createInjector(new ScreamingModule(this));
        enable(injector);
    }

    @Override
    public void onDisable() {
        disable();
    }

    @Override
    public void onLoad() {
        load();
    }

    @Override
    public Logger getLog() {
        return getSLF4JLogger();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> P getPlugin() {
        return (P) this;
    }

    @Override
    public String getPluginName() {
        return getDescription().getName();
    }

    @Override
    public Path getPluginFolder() {
        return getDataFolder().toPath();
    }

    @Override
    public SenderWrapper<?> getConsoleWrapper() {
        return SenderWrapper.of(getServer().getConsoleSender());
    }

    @Override
    public Optional<PlayerWrapper<?>> getWrapperFor(UUID uuid) {
        final var player = getServer().getPlayer(uuid);

        if (player == null) {
            return Optional.empty();
        }

        return Optional.of(PlayerWrapper.of(player));
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return getServer().getPluginManager().isPluginEnabled(pluginName);
    }
}
