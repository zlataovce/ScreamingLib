package org.screamingsandals.lib.visual;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.core.ScreamingModule;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

@RequiredArgsConstructor
public class VisualsModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        install(new ScreamingModule(pluginWrapper));
    }
}
