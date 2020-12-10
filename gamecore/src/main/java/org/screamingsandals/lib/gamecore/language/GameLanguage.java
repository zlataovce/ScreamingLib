package org.screamingsandals.lib.gamecore.language;


import org.screamingsandals.lib.core.lang.LanguageBase;
import org.screamingsandals.lib.core.lang.registry.FileRegistry;
import org.screamingsandals.lib.core.lang.registry.LanguageRegistry;
import org.screamingsandals.lib.core.lang.registry.PlayerRegistry;
import org.screamingsandals.lib.core.wrapper.plugin.PluginWrapper;

public class GameLanguage extends LanguageBase {

    public GameLanguage(PluginWrapper pluginWrapper, FileRegistry fileRegistry, LanguageRegistry languageRegistry, PlayerRegistry playerRegistry) {
        super(pluginWrapper, fileRegistry, languageRegistry, playerRegistry);
    }

    public static GameMessage mpr() {
        return m(null, null, true);
    }

    public static GameMessage mpr(String key) {
        return m(key, null, true);
    }

    public static GameMessage mpr(String key, String def) {
        return m(key, def, true);
    }


    public static GameMessage m() {
        return m(null, null, false);
    }

    public static GameMessage m(String key) {
        return m(key, null, false);
    }

    public static GameMessage m(String key, boolean prefix) {
        return m(key, null, prefix);
    }

    public static GameMessage m(String key, String def) {
        return m(key, def, false);
    }

    public static GameMessage m(String key, String def, boolean prefix) {
        return new GameMessage(key, def, prefix);
    }
}
