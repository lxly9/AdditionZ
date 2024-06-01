package net.additionz.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.additionz.misc.FletchingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FletchingReiDisplay extends BasicDisplay {

    public FletchingReiDisplay(RecipeEntry<FletchingRecipe> recipe) {
        this(EntryIngredients.ofIngredients(recipe.value().getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.value().getResult(BasicDisplay.registryAccess()))),
                Optional.ofNullable(null));// recipe.value().getId()
    }

    public FletchingReiDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AdditionzReiPlugin.FLETCHING;
    }

    public static BasicDisplay.Serializer<FletchingReiDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(FletchingReiDisplay::new);
    }
}
