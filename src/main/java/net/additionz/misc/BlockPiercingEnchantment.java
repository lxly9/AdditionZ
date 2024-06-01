package net.additionz.misc;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class BlockPiercingEnchantment extends Enchantment {

    public BlockPiercingEnchantment() {
        super(Enchantment.properties(ConventionalItemTags.BOWS_TOOLS, 5, 3, Enchantment.leveledCost(1, 11), Enchantment.leveledCost(21, 11), 1, EquipmentSlot.MAINHAND));
    }

}
