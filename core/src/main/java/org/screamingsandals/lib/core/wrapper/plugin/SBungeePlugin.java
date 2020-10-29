package org.screamingsandals.lib.core.wrapper.plugin;

import com.google.inject.Injector;
import com.google.inject.Module;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

public abstract class SBungeePlugin extends Plugin implements PluginWrapper {

    public abstract List<Module> modules();

    public abstract void enable(Injector injector);

    public abstract void disable();

    public abstract void load();

    @Override
    public void onEnable() {
        enable(PluginHelper.createInjector(modules(), this));
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
        return SenderWrapper.of(getProxy().getConsole());
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return getProxy().getPluginManager().getPlugin(pluginName) != null;
    }

    @Override
    public PluginType getType() {
        return PluginType.BUNGEE;
    }

    @Override
    public void registerListener(Object listener) {
        try {
            getSLF4JLogger().debug("Registering new listener");
            getProxy().getPluginManager().registerListener(getPlugin(), (Listener) listener);
        } catch (Exception e) {
            getSLF4JLogger().warn("Not instance of listener", e);
        }
    }
}
