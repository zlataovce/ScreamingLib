package org.screamingsandals.lib.bukkit.material.attribute;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.screamingsandals.lib.bukkit.material.slot.BukkitEquipmentSlotMapping;
import org.screamingsandals.lib.material.attribute.*;
import org.screamingsandals.lib.material.slot.EquipmentSlotMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        BukkitAttributeTypeMapping.class,
        BukkitEquipmentSlotMapping.class
})
public class BukkitAttributeMapping extends AttributeMapping {
    public static void init() {
        AttributeMapping.init(BukkitAttributeMapping::new);
    }

    public BukkitAttributeMapping() {
        InitUtils.doIfNot(BukkitAttributeTypeMapping::isInitialized, BukkitAttributeTypeMapping::init);
        InitUtils.doIfNot(BukkitEquipmentSlotMapping::isInitialized, BukkitEquipmentSlotMapping::init);

        attributeModifierConverter
                .registerW2P(AttributeModifier.class, holder ->
                        new AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                        )
                )
                .registerP2W(AttributeModifier.class, attributeModifier ->
                        new AttributeModifierHolder(
                                attributeModifier.getUniqueId(),
                                attributeModifier.getName(),
                                attributeModifier.getAmount(),
                                AttributeModifierHolder.Operation.byOrdinal(attributeModifier.getOperation().ordinal())
                        )
                );

        itemAttributeConverter
                .registerW2P(BukkitItemAttribute.class, holder -> {
                    AttributeModifier modifier;
                    try {
                        modifier = new AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                AttributeModifier.Operation.values()[holder.getOperation().ordinal()],
                                holder.getSlot() != null ? holder.getSlot().as(EquipmentSlot.class) : null
                        );
                    } catch (Throwable throwable) {
                        modifier = new AttributeModifier(
                                holder.getUuid(),
                                holder.getName(),
                                holder.getAmount(),
                                AttributeModifier.Operation.values()[holder.getOperation().ordinal()]
                        );
                    }
                    return new BukkitItemAttribute(holder.getType().as(Attribute.class), modifier);
                })
                .registerP2W(BukkitItemAttribute.class, bukkitItemAttribute -> {
                    try {
                        return new ItemAttributeHolder(
                                AttributeTypeMapping.resolve(bukkitItemAttribute.getAttribute()).orElseThrow(),
                                bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                bukkitItemAttribute.getAttributeModifier().getName(),
                                bukkitItemAttribute.getAttributeModifier().getAmount(),
                                AttributeModifierHolder.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                EquipmentSlotMapping.resolve(bukkitItemAttribute.getAttributeModifier().getOperation()).orElse(null) // nullable
                        );
                    } catch (Throwable throwable) {
                        return new ItemAttributeHolder(
                                AttributeTypeMapping.resolve(bukkitItemAttribute.getAttribute()).orElseThrow(),
                                bukkitItemAttribute.getAttributeModifier().getUniqueId(),
                                bukkitItemAttribute.getAttributeModifier().getName(),
                                bukkitItemAttribute.getAttributeModifier().getAmount(),
                                AttributeModifierHolder.Operation.values()[bukkitItemAttribute.getAttributeModifier().getOperation().ordinal()],
                                null
                        );
                    }
                });
    }

    @Override
    protected Optional<AttributeHolder> wrapAttribute0(Object attribute) {
        if (attribute instanceof AttributeInstance) {
            return Optional.of(new BukkitAttributeHolder((AttributeInstance) attribute));
        }
        return Optional.empty();
    }
}
