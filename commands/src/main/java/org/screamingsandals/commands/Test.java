package org.screamingsandals.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.screamingsandals.commands.guice.CommandModule;
import org.screamingsandals.lib.core.config.SConfig;
import org.screamingsandals.lib.core.config.exception.SConfigException;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;
import org.screamingsandals.lib.core.wrapper.sender.PlayerWrapper;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.screamingsandals.lib.core.lang.SLang.mpr;

@Plugin(id = "test", name = "Test", description = "This is a test")
public class Test implements PluginWrapper {
    private final ProxyServer proxyServer;
    private final PluginDescription pluginDescription;
    private final Path pluginFolder;
    private final Logger logger;
    private final Injector velocityInjector;

    private SenderWrapper<CommandSource> senderWrapper;

    @Inject
    public Test(ProxyServer proxyServer, PluginDescription pluginDescription,
                @DataDirectory Path pluginFolder, Logger logger,
                Injector velocityInjector) {
        this.proxyServer = proxyServer;
        this.pluginDescription = pluginDescription;
        this.pluginFolder = pluginFolder;
        this.logger = logger;
        this.velocityInjector = velocityInjector;
    }

    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        final var injector = velocityInjector.createChildInjector(
                new CommandModule(this));
        senderWrapper = SenderWrapper.of(proxyServer.getConsoleCommandSource());

        final var newPath = pluginFolder.resolve("test.conf");
        try {
            final var config = SConfig.create(SConfig.Format.HOCON, newPath);

            final var component = MiniMessage.get()
                    .parse("<blue>You are a <yellow>retard.")
                    .asComponent();
            final var node = config.root();

            node.set(Component.class, component);
            config.save();

            senderWrapper.sendMessage(component);
        } catch (SConfigException | SerializationException e) {
            e.printStackTrace();
        }

        mpr("kokotinec2")
                .replace("kokotina", "ty jsi piča")
                .send();

    }

    @Override
    public Logger getLog() {
        return logger;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P> P getPlugin() {
        return (P) this;
    }

    @Override
    public String getPluginName() {
        return pluginDescription.getName()
                .orElse(pluginDescription.getId());
    }

    @Override
    public Path getPluginFolder() {
        return pluginFolder;
    }

    @Override
    public SenderWrapper<?> getConsoleWrapper() {
        return senderWrapper;
    }

    @Override
    public Optional<PlayerWrapper<?>> getWrapperFor(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return proxyServer.getPluginManager().getPlugin(pluginName).isPresent();
    }
}
