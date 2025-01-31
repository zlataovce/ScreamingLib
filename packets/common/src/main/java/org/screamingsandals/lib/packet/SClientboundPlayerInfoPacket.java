package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundPlayerInfoPacket extends AbstractPacket {
    private Action action;
    private List<PlayerInfoData> data;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVarInt(action.ordinal());
        writer.writeSizedCollection(data, playerInfoData -> {
            writer.writeUuid(playerInfoData.uuid());
            switch (action) {
                case ADD_PLAYER: {
                    writer.writeSizedString(playerInfoData.realName());
                    writer.writeSizedCollection(playerInfoData.properties(), property -> {
                        writer.writeSizedString(property.name());
                        writer.writeSizedString(property.value());
                        var hasSignature = property.hasSignature();
                        writer.writeBoolean(hasSignature);
                        if (hasSignature) {
                            writer.writeSizedString(property.signature());
                        }
                    });
                    writer.writeVarInt(playerInfoData.gameMode().getId());
                    writer.writeVarInt(playerInfoData.latency());
                    writer.writeBoolean(playerInfoData.displayName() != null);
                    if (playerInfoData.displayName() != null) {
                        writer.writeComponent(playerInfoData.displayName());
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    writer.writeVarInt(playerInfoData.gameMode().getId());
                    break;
                }
                case UPDATE_LATENCY: {
                    writer.writeVarInt(playerInfoData.latency());
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    writer.writeBoolean(playerInfoData.displayName() != null);
                    if (playerInfoData.displayName() != null) {
                        writer.writeComponent(playerInfoData.displayName());
                    }
                    break;
                }
            }
        });
    }

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }

    @Data
    public static class PlayerInfoData {
        private final UUID uuid;
        private final String realName;
        private final int latency;
        private final GameModeHolder gameMode;
        private final Component displayName;
        private final List<Property> properties;
    }

    @Data
    @RequiredArgsConstructor
    public static class Property {
        private final String name;
        private final String value;
        @Nullable
        private final String signature;

        public Property(String name, String value) {
            this(name, value, null);
        }

        public boolean hasSignature() {
            return signature != null;
        }
    }
}
