package org.screamingsandals.lib.bukkit.item.meta;

import org.bukkit.enchantments.Enchantment;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.EnchantmentMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;

@Service
public class BukkitEnchantmentMapping extends EnchantmentMapping {

    public BukkitEnchantmentMapping() {
        enchantmentConverter
                .registerW2P(Enchantment.class, e -> Enchantment.getByName(e.getPlatformName()))
                .registerP2W(Enchantment.class, e -> new EnchantmentHolder(e.getName()));

        Arrays.stream(Enchantment.values()).forEach(enchantment -> mapping.put(NamespacedMappingKey.of(enchantment.getName()), new EnchantmentHolder(enchantment.getName())));
    }

}
