package net.additionz.misc;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import java.util.stream.Stream;

import net.additionz.AdditionMain;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class FletchingRecipe implements Recipe<Inventory> {

    private final Ingredient bottom;
    private final Ingredient middle;
    private final Ingredient top;
    private final Optional<ItemStack> addition;
    private final ItemStack result;

    public FletchingRecipe(Ingredient bottom, Ingredient middle, Ingredient top, Optional<ItemStack> addition, ItemStack result) {
        this.bottom = bottom;
        this.middle = middle;
        this.top = top;
        this.addition = addition.isPresent() ? addition : Optional.of(ItemStack.EMPTY);
        this.result = result;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        if (this.addition.isPresent() && !this.addition.get().isEmpty() && !ItemStack.areItemsAndComponentsEqual(this.addition.get(), inventory.getStack(3))) {
            return false;
        }
        return this.bottom.test(inventory.getStack(2)) && this.middle.test(inventory.getStack(1)) && this.top.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory, WrapperLookup wrapperLookup) {
        ItemStack itemStack = this.result.copy();
        if (!inventory.getStack(1).getComponentChanges().isEmpty()) {
            itemStack = inventory.getStack(1).copyComponentsToNewStack(this.result.getItem(), this.result.getCount());
            itemStack.applyUnvalidatedChanges(this.result.getComponentChanges());
        }
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(4);
        ingredients.add(bottom);
        ingredients.add(middle);
        ingredients.add(top);
        ingredients.add(Ingredient.ofStacks(addition.get()));
        return ingredients;
    }

    @Override
    public ItemStack getResult(WrapperLookup wrapperLookup) {
        return this.result;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Blocks.FLETCHING_TABLE);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AdditionMain.FLETCHING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return AdditionMain.FLETCHING_RECIPE;
    }

    // Used by ClientRecipeBook
    @Override
    public boolean isEmpty() {
        if (!this.addition.isEmpty() && !this.addition.get().isEmpty()) {
            return false;
        }
        return Stream.of(this.bottom, this.middle, this.top).anyMatch(ingredient -> ingredient.getMatchingStacks().length == 0);
    }

    public boolean hasAddition() {
        return !this.addition.isEmpty() && !this.addition.get().isEmpty();
    }

    public static class Serializer implements RecipeSerializer<FletchingRecipe> {

        public static final MapCodec<FletchingRecipe> CODEC = RecordCodecBuilder
                .mapCodec(instance -> instance.group((Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("bottom")).forGetter(recipe -> recipe.bottom),
                        (Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("middle")).forGetter(recipe -> recipe.middle), (Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("top")).forGetter(recipe -> recipe.top),
                        (ItemStack.OPTIONAL_CODEC.optionalFieldOf("addition")).forGetter(recipe -> recipe.addition), (ItemStack.VALIDATED_CODEC.fieldOf("result")).forGetter(recipe -> recipe.result))
                        .apply(instance, FletchingRecipe::new));

        public static final PacketCodec<RegistryByteBuf, FletchingRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::write, Serializer::read);

        @Override
        public MapCodec<FletchingRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, FletchingRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static FletchingRecipe read(RegistryByteBuf buf) {
            Ingredient topIngredient = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient middleIngredient = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient bottomIngredient = Ingredient.PACKET_CODEC.decode(buf);
            Optional<ItemStack> additionItemStack = Optional.of(ItemStack.OPTIONAL_PACKET_CODEC.decode(buf));
            ItemStack itemStack = ItemStack.PACKET_CODEC.decode(buf);
            return new FletchingRecipe(bottomIngredient, middleIngredient, topIngredient, additionItemStack, itemStack);
        }

        private static void write(RegistryByteBuf buf, FletchingRecipe fletchingRecipe) {
            Ingredient.PACKET_CODEC.encode(buf, fletchingRecipe.bottom);
            Ingredient.PACKET_CODEC.encode(buf, fletchingRecipe.middle);
            Ingredient.PACKET_CODEC.encode(buf, fletchingRecipe.top);
            ItemStack.OPTIONAL_PACKET_CODEC.encode(buf, fletchingRecipe.addition.get());
            ItemStack.PACKET_CODEC.encode(buf, fletchingRecipe.result);
        }

    }

}
