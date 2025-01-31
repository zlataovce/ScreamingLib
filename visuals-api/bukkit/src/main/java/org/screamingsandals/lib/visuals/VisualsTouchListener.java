package org.screamingsandals.lib.visuals;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.utils.nms.network.AutoPacketInboundListener;
import org.screamingsandals.lib.nms.accessors.ServerboundInteractPacketAccessor;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.utils.reflect.Reflect;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisualsTouchListener<T extends TouchableVisual<T>> {
    private final Map<UUID, Long> coolDownMap = new HashMap<>();

    public VisualsTouchListener(AbstractVisualsManager<T> manager, Plugin plugin) {
        new AutoPacketInboundListener(plugin) {
            @Override
            protected Object handle(Player sender, Object packet) {
                if (ServerboundInteractPacketAccessor.getType().isInstance(packet)) {
                    final var entityId = (int) Reflect.getField(packet, ServerboundInteractPacketAccessor.getFieldEntityId());
                    final var activeVisuals = manager.getActiveVisuals();

                    for (var entry : activeVisuals.entrySet()) {
                        var visual = entry.getValue();
                        if (visual.hasId(entityId) && visual.isTouchable()) {
                            synchronized (coolDownMap) {
                                if (coolDownMap.containsKey(sender.getUniqueId())) {
                                    final var lastClick = coolDownMap.get(sender.getUniqueId());
                                    if (System.currentTimeMillis() - lastClick < visual.getClickCoolDown()) {
                                        break;
                                    }
                                }
                                coolDownMap.put(sender.getUniqueId(), System.currentTimeMillis());
                            }

                            manager.fireVisualTouchEvent(PlayerMapper.wrapPlayer(sender), visual, packet);
                            break;
                        }
                    }
                }
                return packet;
            }
        };
    }
}
