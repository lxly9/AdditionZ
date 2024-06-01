package net.additionz.misc;

import net.additionz.AdditionMain;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpyglassItem;

public class EagleEyedEnchantment extends Enchantment {

    public EagleEyedEnchantment() {
        super(Enchantment.properties(AdditionMain.SPYGLASSES, 5, 1, Enchantment.leveledCost(1, 11), Enchantment.leveledCost(21, 11), 1, EquipmentSlot.MAINHAND));
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SpyglassItem;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

}
