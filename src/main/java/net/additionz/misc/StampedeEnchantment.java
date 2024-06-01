package net.additionz.misc;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

public class StampedeEnchantment extends Enchantment {

    public StampedeEnchantment() {
        super(Enchantment.properties(ConventionalItemTags.SHIELDS_TOOLS, 5, 3, Enchantment.leveledCost(1, 11), Enchantment.leveledCost(21, 11), 1, EquipmentSlot.OFFHAND));
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

}
