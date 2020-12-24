package org.screamingsandals.lib.nms.entity;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.lib.reflect.SReflect;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.getHandle;
import static org.screamingsandals.lib.reflect.SReflect.*;

public class ArmorStandNMS extends EntityNMS {

    public ArmorStandNMS(Object handler) {
        super(handler);
        if (!EntityArmorStand.isInstance(handler)) {
            throw new IllegalArgumentException("Entity must be instance of EntityArmorStand!!");
        }
    }

    public ArmorStandNMS(ArmorStand stand) {
        this(getHandle(stand));
    }

    public ArmorStandNMS(Location loc) throws Throwable {
        this(EntityArmorStand.getConstructor(World, double.class, double.class, double.class)
                .newInstance(getMethod(loc.getWorld(), "getHandle").invoke(), loc.getX(), loc.getY(), loc.getZ()));
        this.setLocation(loc); // Update rotation
    }

    public void setHelmet(ItemStack itemStack) {
        final var headSlot = findEnumConstant(EnumItemSlot, "HEAD");
        getMethod(getField(handler, "equipment"), "armorItems", EnumItemSlot, boolean.class)
                .invoke(headSlot, getHandle(itemStack), true);
    }

    public ItemStack getHelmet() {
        final var headSlot = findEnumConstant(EnumItemSlot, "HEAD");
        return (ItemStack) getMethod(getField(handler, "armorItems"), "getSlot", EnumItemSlot).invoke(headSlot);
    }

    public void setSmall(boolean small) {
        getMethod(handler, "setSmall,func_175420_a", boolean.class).invoke(small);
    }

    public boolean isSmall() {
        return (boolean) SReflect.fastInvoke(handler, "isSmall,func_175410_n");
    }

    public void setArms(boolean arms) {
        getMethod(handler, "setArms,func_175413_k", boolean.class).invoke(arms);
    }

    public boolean isArms() {
        return (boolean) SReflect.fastInvoke(handler, "hasArms,func_175402_q");
    }

    public void setBasePlate(boolean basePlate) {
        getMethod(handler, "setBasePlate,func_175426_l", boolean.class).invoke(basePlate);
    }

    public boolean isBasePlate() {
        return (boolean) SReflect.fastInvoke(handler, "hasBasePlate,func_175414_r");
    }

    public void setMarker(boolean marker) {
        getMethod(handler, "setMarker,func_181027_m,n", boolean.class).invoke(marker);
    }

    public boolean isMarker() {
        return (boolean) SReflect.fastInvoke(handler, "isMarker,func_175426_l,s");
    }


}
