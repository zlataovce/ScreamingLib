package org.screamingsandals.lib.item.builder;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.attribute.AttributeMapping;
import org.screamingsandals.lib.item.meta.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ItemBuilder. Modifies or creates new Item. Simple, right?
 */
@RequiredArgsConstructor
public class ItemBuilder {
    @NotNull
    private final Item item;

    public static ItemBuilder of(ItemTypeHolder materialHolder) {
        return new ItemBuilder(materialHolder);
    }

    /**
     * Creates new ItemBuilder.
     *
     * @param material material to build item from
     */
    public ItemBuilder(ItemTypeHolder material) {
        this.item = new Item();
        item.setMaterial(material);
    }

    /**
     * Sets new type of the item
     *
     * @param type Anything that can be an Item. Name, ItemStack, serialized ItemStack and so on.
     * @return this item builder
     */
    public ItemBuilder type(@NotNull Object type) {
        ItemFactory.readShortStack(item, type);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(@NotNull Component name) {
        item.setDisplayName(name);
        return this;
    }

    public ItemBuilder name(@NotNull ComponentLike name) {
        item.setDisplayName(name.asComponent());
        return this;
    }

    public ItemBuilder name(@Nullable String name) {
        if (name == null) {
            item.setDisplayName(null);
            return this;
        }
        return name(AdventureHelper.toComponent(name));
    }

    public ItemBuilder localizedName(@Nullable ComponentLike name) {
        item.setLocalizedName(name != null ? name.asComponent() : null);
        return this;
    }

    public ItemBuilder localizedName(@Nullable Component name) {
        item.setLocalizedName(name);
        return this;
    }

    public ItemBuilder localizedName(@Nullable String name) {
        if (name == null) {
            item.setLocalizedName(null);
            return this;
        }
        return localizedName(AdventureHelper.toComponent(name));
    }

    public ItemBuilder customModelData(Integer data) {
        item.setCustomModelData(data);
        return this;
    }

    public ItemBuilder repair(int repair) {
        item.setRepair(repair);
        return this;
    }

    public ItemBuilder flags(@Nullable List<Object> flags) {
        if (flags == null) {
            return this;
        } else {
            item.getItemFlags().addAll(flags.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()));
        }
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        item.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder lore(@NotNull Component lore) {
        item.addLore(lore);
        return this;
    }

    public ItemBuilder lore(@NotNull ComponentLike lore) {
        item.addLore(lore.asComponent());
        return this;
    }

    public ItemBuilder lore(@Nullable String lore) {
        if (lore == null) {
            item.addLore(Component.empty());
            return this;
        }
        return lore(AdventureHelper.toComponent(lore));
    }

    public <C extends ComponentLike> ItemBuilder lore(@NotNull List<C> lore) {
        item.getLore().addAll(lore.stream().map(ComponentLike::asComponent).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder enchant(@NotNull Object enchant, int level) {
        enchant(enchant + " " + level);
        return this;
    }

    public ItemBuilder enchant(@NotNull Map<Object, Integer> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    public ItemBuilder enchant(@NotNull List<Object> enchants) {
        enchants.forEach(this::enchant);
        return this;
    }

    public ItemBuilder enchant(@NotNull Object enchant) {
        EnchantmentHolder.ofOptional(enchant).ifPresent(item::addEnchant);
        return this;
    }

    public ItemBuilder potion(@NotNull Object potion) {
        PotionHolder.ofOptional(potion).ifPresent(item::setPotion);
        return this;
    }

    public ItemBuilder attribute(@NotNull Object itemAttribute) {
        AttributeMapping.wrapItemAttribute(itemAttribute).ifPresent(item::addItemAttribute);
        return this;
    }

    public ItemBuilder effect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> PotionEffectHolder.ofOptional(effect1).ifPresent(item::addPotionEffect));
            return this;
        }

        PotionEffectHolder.ofOptional(effect).ifPresent(item::addPotionEffect);
        return this;
    }

    public ItemBuilder recipe(@NotNull String key) {
        return recipe(NamespacedMappingKey.of(key));
    }

    public ItemBuilder recipe(@NotNull NamespacedMappingKey key) {
        item.addRecipe(key);
        return this;
    }

    public ItemBuilder color(@NotNull String color) {
        var c = TextColor.fromCSSHexString(color);
        if (c != null) {
            return color(c);
        } else {
            var c2 = NamedTextColor.NAMES.value(color.toLowerCase().trim());
            if (c2 != null) {
                return color(c2);
            }
        }
        return this;
    }

    public ItemBuilder color(@NotNull RGBLike color) {
        item.setColor(color);
        return this;
    }

    public ItemBuilder color(int r, int g, int b) {
        item.setColor(TextColor.color(r, g, b));
        return this;
    }

    public ItemBuilder skullOwner(@Nullable String skullOwner) {
        item.setSkullOwner(skullOwner);
        return this;
    }

    public ItemBuilder fireworkEffect(@NotNull Object effect) {
        if (effect instanceof List) {
            final var list = (List<?>) effect;
            list.forEach(effect1 -> FireworkEffectHolder.ofOptional(effect1).ifPresent(item::addFireworkEffect));
            return this;
        }

        FireworkEffectHolder.ofOptional(effect).ifPresent(item::addFireworkEffect);
        return this;
    }

    // For legacy versions
    @Deprecated
    public ItemBuilder damage(int damage) {
        return durability(damage);
    }

    // Or (durability is just alias for damage)
    public ItemBuilder durability(int durability) {
        item.setMaterial(item.getMaterial().withDurability((short) durability));
        return this;
    }

    public Optional<Item> build() {
        if (item.getMaterial() != null) {
            return Optional.of(item);
        }

        return Optional.empty();
    }
}
