package net.additionz.misc;

import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record FletchingRecipeInput(ItemStack bottom, ItemStack middle, ItemStack top, @Nullable ItemStack addition) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
        case 0 -> this.bottom;
        case 1 -> this.middle;
        case 2 -> this.top;
        case 3 -> this.addition;
        default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        };
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        if (this.addition != null && !this.addition.isEmpty()) {
            return false;
        }
        return this.bottom.isEmpty() && this.middle.isEmpty() && this.top.isEmpty();
    }
}
