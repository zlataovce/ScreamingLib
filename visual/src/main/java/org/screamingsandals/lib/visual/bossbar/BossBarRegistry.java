package org.screamingsandals.lib.visual.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.core.wrapper.sender.PlayerWrapper;

public interface BossBarRegistry {

    void register(PlayerWrapper<?> player, BossBar bar);

    void remove(PlayerWrapper<?> player);
}
