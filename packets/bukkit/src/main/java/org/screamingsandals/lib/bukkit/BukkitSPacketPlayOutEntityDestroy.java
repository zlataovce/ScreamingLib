package org.screamingsandals.lib.bukkit;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutEntityDestroy;

public class BukkitSPacketPlayOutEntityDestroy extends BukkitSPacket implements SPacketPlayOutEntityDestroy {

    public BukkitSPacketPlayOutEntityDestroy() {
        super(ClassStorage.NMS.PacketPlayOutEntityDestroy);
    }

    @Override
    public void setEntitiesToDestroy(int[] entityIdArray) {
        packet.setField("a", entityIdArray);
    }
}
