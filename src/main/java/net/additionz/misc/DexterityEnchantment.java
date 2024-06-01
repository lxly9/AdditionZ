package net.additionz.misc;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.tag.ItemTags;

public class DexterityEnchantment extends Enchantment {

    public DexterityEnchantment() {
        super(Enchantment.properties(ItemTags.FOOT_ARMOR, 5, 3, Enchantment.leveledCost(1, 11), Enchantment.leveledCost(21, 11), 1, EquipmentSlot.FEET));
    }

}
