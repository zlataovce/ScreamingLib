package org.screamingsandals.lib.visual;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.screamingsandals.commands.guice.CommandModule;
import org.screamingsandals.lib.core.wrapper.plugin.SBukkitPlugin;

import java.util.List;

public class Test extends SBukkitPlugin {
    @Override
    public List<Module> modules() {
        return List.of(new CommandModule(this));
    }

    @Override
    public void enable(Injector injector) {
        getSLF4JLogger().info("Loaded!");
    }

    @Override
    public void disable() {
        getSLF4JLogger().info("Disabled!");
    }

    @Override
    public void load() {

    }
}
