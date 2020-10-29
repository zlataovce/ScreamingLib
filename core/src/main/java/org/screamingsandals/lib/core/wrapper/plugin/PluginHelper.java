package org.screamingsandals.lib.core.wrapper.plugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.experimental.UtilityClass;
import org.screamingsandals.lib.core.ScreamingModule;

import java.util.LinkedList;
import java.util.List;

@UtilityClass
class PluginHelper {

    static Injector createInjector(List<Module> newModules, PluginWrapper wrapper) {
        final var screamingModule = new ScreamingModule(wrapper);
        var modules = newModules;

        if (modules == null) {
            modules = List.of(screamingModule);
        } else {
            modules = new LinkedList<>(modules);
            modules.add(screamingModule);
        }

        return Guice.createInjector(modules.toArray(Module[]::new));
    }
}
