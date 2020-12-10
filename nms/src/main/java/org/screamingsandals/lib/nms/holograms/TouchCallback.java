package org.screamingsandals.lib.nms.holograms;

import org.bukkit.entity.Player;

public interface TouchCallback {
	void call(Player player, Hologram hologram);
}
