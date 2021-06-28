package org.screamingsandals.lib.bukkit.packet;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutEntityEquipment;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;

public class BukkitSPacketPlayOutEntityEquipment extends BukkitSPacket implements SPacketPlayOutEntityEquipment {

    public BukkitSPacketPlayOutEntityEquipment() {
        super(ClassStorage.NMS.PacketPlayOutEntityEquipment);
    }

    @Override
    public void setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("b", entityId);
        } else {
            packet.setField("a,field_149394_a", entityId);
        }
    }

    @Override
    public void setItemAndSlot(Item item, Slot slot) {
        if (slot == null) {
            throw new UnsupportedOperationException("Slot cannot be null!");
        }
        if (item == null) {
            item = ItemFactory.build(Material.AIR).orElseThrow();
        }
        if (isOldPacket()) {
            packet.setField("c", ClassStorage.stackAsNMS(item.as(ItemStack.class)));
            packet.setField("b", getSlot(slot));
        } else {
            final var data = List.of(Pair.of(getSlot(slot), ClassStorage.stackAsNMS(item.as(ItemStack.class))));
            if (Version.isVersion(1, 17)) {
                packet.setField("c", data);
            } else {
                packet.setField("b,field_241789_b_", data);
            }
        }
    }

    protected Object getSlot(Slot slot) {
        if (slot == null) {
            throw new UnsupportedOperationException("Slot cannot be null!");
        }
        EquipmentSlot bukkitSlot = EquipmentSlot.valueOf(slot.name().toUpperCase());
        return Reflect.getMethod(ClassStorage.NMS.CraftEquipmentSlot, "getNMS", EquipmentSlot.class).invokeStatic(bukkitSlot);
    }

    protected boolean isOldPacket() {
        return Reflect.constructor(ClassStorage.NMS.PacketPlayOutEntityEquipment, int.class, List.class).isEmpty();
    }
}
