package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutUnloadChunk;

public class BukkitSPacketPlayOutUnloadChunk extends BukkitSPacket implements SPacketPlayOutUnloadChunk {
    public BukkitSPacketPlayOutUnloadChunk() {
        super(ClassStorage.NMS.PacketPlayOutUnloadChunk);
    }

    @Override
    public void setChunkX(int chunkX) {
        packet.setField("a,field_186942_a", chunkX);
    }

    @Override
    public void setChunkZ(int chunkZ) {
        packet.setField("b,field_186943_b", chunkZ);
    }
}
