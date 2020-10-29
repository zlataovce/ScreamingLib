package org.screamingsandals.lib.core.wrapper.sender;

import com.google.inject.Inject;
import net.kyori.adventure.platform.AudienceProvider;

public class SenderWrapperService {
    private final AudienceProvider audienceProvider;

    private static SenderWrapperService instance;

    @Inject
    public SenderWrapperService(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
        instance = this;
    }

    public static <K> SenderWrapper<K> wrap(K sender) {
        return new AbstractSender<>(sender, instance.audienceProvider.console()) {
        };
    }
}
