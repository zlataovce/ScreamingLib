package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundEntityEventPacket extends AbstractPacket {
    private int entityId;
    private Status status;

    @Override
    public void write(PacketWriter writer) {
        writer.writeInt(entityId);
        if (writer.protocol() < 210 && (status == Status.SMOKE || status == Status.HURT)) {
            if (status == Status.SMOKE) {
                writer.writeByte((byte) 7);
            } else {
                writer.writeByte((byte) 6);
            }
        } else {
            writer.writeByte((byte) status.ordinal());
        }
    }

    public enum Status {
        ARROW,
        RABBIT_JUMP,
        HURT,
        DEATH,
        ATTACK,
        UNUSED_1,
        SMOKE, // inverted on older versions
        HEART, // inverted on older versions
        SHAKING_WATER,
        PLAYER_FINISHED_USING,
        GRASS_EATING,
        IRON_GOLEM_FLOWER,
        VILLAGER_MATING,
        VILLAGER_ANGRY,
        VILLAGER_HAPPY,
        WITCH_MAGIC,
        ZOMBIE_CURE,
        FIREWORK_EXPLOSION,
        ANIMAL_IN_LOVE,
        SQUID_ROTATION_RESET,
        SPAWN_EXPLOSION_PARTICLE,
        GUARDIAN_SOUND,
        REDUCED_DEBUG_SCREEN,
        DISABLE_REDUCED_DEBUG_SCREEN,

        // 1.9+
        OP_0,
        OP_1,
        OP_2,
        OP_3,
        OP_4,
        SHIELD_BLOCK,
        SHIELD_BREAK,
        PULL_PLAYER,
        ARMOR_STAND_HIT,
        THORNS_HURT,

        // 1.10+ and then idk xdd
        IRON_GOLEM_UNFLOWER,
        TOTEM,
        DROWN,
        BURN,
        DOLPHIN,
        RAVARGE_STUNNED,
        NEW_OCELOT_TAMING_FAILED,
        NEW_OCELOT_TAMING_SUCCESS,
        VILLAGER_SPLASH,
        PLAYER_CLOUD,
        SWEET_BERRY_HURT,
        FOX_MOUTH,
        CHORUS_TELEPORT,
        MAIN_HAND_BREAK,
        OFF_HAND_BREAK,
        HEAD_SLOT_BREAK,
        CHEST_SLOT_BREAK,
        LEGS_SLOT_BREAK,
        FEET_SLOT_BREAK,
        HONEY_SLIDE,
        HONEY_FALL,
        HAND_SWAP
    }
}
