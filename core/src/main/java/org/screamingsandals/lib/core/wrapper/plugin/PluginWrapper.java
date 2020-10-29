package org.screamingsandals.lib.core.wrapper.plugin;

import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.Logger;

import java.nio.file.Path;

public interface PluginWrapper {
    Logger getLog();

    <P> P getPlugin();

    String getPluginName();

    Path getPluginFolder();

    SenderWrapper<?> getConsoleWrapper();

    boolean isPluginEnabled(String pluginName);

    PluginType getType();

    void registerListener(Object listener);
}
