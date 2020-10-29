package org.screamingsandals.lib.core.wrapper.plugin;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

public abstract class SBukkitPlugin extends JavaPlugin implements PluginWrapper {

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
        //todo: support spigot
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
    public boolean isPluginEnabled(String pluginName) {
        return getServer().getPluginManager().isPluginEnabled(pluginName);
    }

    @Override
    public PluginType getType() {
        return PluginType.BUKKIT;
    }

    @Override
    public void registerListener(Object listener) {
        if (listener instanceof Listener) {
            getServer().getPluginManager().registerEvents((Listener) listener, getPlugin());
        }
    }
}
