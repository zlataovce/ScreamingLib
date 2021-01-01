package org.screamingsandals.lib.material;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.meta.EnchantmentHolder;
import org.screamingsandals.lib.material.meta.PotionHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Item implements Cloneable, Wrapper {
    //@Nullable // in initial state it's null
    private MaterialHolder material;
    @Nullable
    private String displayName;
    @Nullable
    private String localizedName;
    private int amount = 1;
    private int customModelData;
    private int repair;
    private boolean unbreakable;
    @Nullable
    private List<String> lore;
    private final List<EnchantmentHolder> enchantments = new ArrayList<>();
    @Nullable
    private List<String> itemFlags;
    @Nullable
    private PotionHolder potion;

    @Deprecated
    @Nullable
    private Object platformMeta;

    public <R> R as(Class<R> type) {
        return ItemFactory.convertItem(this, type);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Item clone() {
        Item item = new Item();
        item.setMaterial(material);
        item.setDisplayName(displayName);
        item.setLocalizedName(localizedName);
        item.setAmount(amount);
        item.setCustomModelData(customModelData);
        item.setRepair(repair);
        item.setUnbreakable(unbreakable);
        item.setLore(lore);
        enchantments.forEach(item.getEnchantments()::add);
        item.setItemFlags(itemFlags);
        item.setPotion(potion);
        item.setPlatformMeta(platformMeta);
        return item;
    }

    public boolean isSimilar(Item item) {
        if (item == null) {
            return false;
        }

        return Objects.equals(item.material, material)
                && Objects.equals(item.displayName, displayName)
                && Objects.equals(item.localizedName, localizedName)
                && customModelData == item.customModelData
                && repair == item.repair
                && unbreakable == item.unbreakable
                && Objects.equals(item.lore, lore)
                && enchantments.equals(item.enchantments)
                && Objects.equals(item.itemFlags, itemFlags)
                && Objects.equals(item.potion, potion);
    }
}
