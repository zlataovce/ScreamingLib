package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.math.Vector3D;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public abstract class SClientboundMoveEntityPacket extends AbstractPacket {
    private int entityId;
    private boolean onGround;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(entityId);
        if (this instanceof Pos) {
            writer.writeMove(((Pos) this).delta);
        }
        if (this instanceof Rot) {
            writer.writeByteRotation(((Rot) this).yaw, ((Rot) this).pitch);
        }
        if (this instanceof PosRot) {
            writer.writeByteRotation(((PosRot) this).yaw, ((PosRot) this).pitch);
        }
        writer.writeBoolean(onGround);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true, fluent = true)
    public static class Rot extends SClientboundMoveEntityPacket {
        private float yaw;
        private float pitch;

        @Override
        public SClientboundMoveEntityPacket.Rot entityId(int entityId) {
            return (Rot) super.entityId(entityId);
        }

        @Override
        public SClientboundMoveEntityPacket.Rot onGround(boolean onGround) {
            return (Rot) super.onGround(onGround);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true, fluent = true)
    public static class Pos extends SClientboundMoveEntityPacket {
        private Vector3D delta;

        @Override
        public SClientboundMoveEntityPacket.Pos entityId(int entityId) {
            return (Pos) super.entityId(entityId);
        }

        @Override
        public SClientboundMoveEntityPacket.Pos onGround(boolean onGround) {
            return (Pos) super.onGround(onGround);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true, fluent = true)
    public static class PosRot extends Pos {
        private float yaw;
        private float pitch;

        @Override
        public SClientboundMoveEntityPacket.PosRot entityId(int entityId) {
            return (PosRot) super.entityId(entityId);
        }

        @Override
        public SClientboundMoveEntityPacket.PosRot onGround(boolean onGround) {
            return (PosRot) super.onGround(onGround);
        }
    }
}
