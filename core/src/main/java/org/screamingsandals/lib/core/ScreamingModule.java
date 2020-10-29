package org.screamingsandals.lib.core;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import org.screamingsandals.lib.core.lang.guice.LanguageModule;
import org.screamingsandals.lib.core.listener.BukkitPlayerListener;
import org.screamingsandals.lib.core.listener.BungeePlayerListener;
import org.screamingsandals.lib.core.listener.VelocityPlayerListener;
import org.screamingsandals.lib.core.papi.PapiModule;
import org.screamingsandals.lib.core.tasker.guice.TaskerModule;
import org.screamingsandals.lib.core.wrapper.player.PlayerWrapperService;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapperService;

/**
 * Main ScreamingLib module.
 * This module contains everything needed in Core that can be initialized
 * via Guice.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ScreamingModule extends AbstractModule {
    private final PluginWrapper pluginWrapper;

    @Override
    protected void configure() {
        bind(PluginWrapper.class).toInstance(pluginWrapper);
        bind(PlayerWrapperService.class).asEagerSingleton();

        switch (pluginWrapper.getType()) {
            case BUKKIT:
                bind(org.bukkit.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginWrapper.getPluginName()))
                        .toInstance(pluginWrapper.getPlugin());
                bind(AudienceProvider.class)
                        .toInstance(BukkitAudiences.create(pluginWrapper.getPlugin()));

                bind(SenderWrapperService.class).asEagerSingleton();
                bind(BukkitPlayerListener.class).asEagerSingleton();
                break;
            case BUNGEE:
                bind(net.md_5.bungee.api.plugin.Plugin.class)
                        .annotatedWith(Names.named(pluginWrapper.getPluginName()))
                        .toInstance(pluginWrapper.getPlugin());
                bind(AudienceProvider.class)
                        .toInstance(BungeeAudiences.create(pluginWrapper.getPlugin()));

                bind(SenderWrapperService.class).asEagerSingleton();
                bind(BungeePlayerListener.class).asEagerSingleton();
                break;
            case VELOCITY:
                bind(VelocityPlayerListener.class).asEagerSingleton();
                break;
            default:
                throw new UnsupportedOperationException("This is totally not supported.");
        }

        install(new TaskerModule(pluginWrapper));
        install(new PapiModule(pluginWrapper));
        install(new LanguageModule());
    }
}
