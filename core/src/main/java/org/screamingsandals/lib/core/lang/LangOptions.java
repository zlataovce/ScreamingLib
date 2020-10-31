package org.screamingsandals.lib.core.lang;

import lombok.Data;
import org.screamingsandals.lib.core.lang.storage.LanguageContainer;

import java.io.File;

@Data
public class LangOptions {
    public static String FALLBACK_LANGUAGE = "en";
    public static String FALLBACK_PREFIX = "[SLang]";

    private String defaultLanguage = "en";
    private LanguageContainer defaultContainer;
    private String customPrefix;
    private File customDataFolder;

    public static LangOptions defaultOptions() {
        return new LangOptions();
    }
}
