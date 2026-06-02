package com.dontnag.bananabaking.recipe;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Collectors;

public abstract class CookingRecipe implements Recipe<CookingRecipeInput> {

    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final Integer cookingTime;

    public CookingRecipe(Integer cookingTime, ItemStack result, List<Ingredient> ingredients) {
        this.result = result;
        this.ingredients = NonNullList.of(
            Ingredient.EMPTY,
            ingredients.toArray(Ingredient[]::new)
        );
        this.cookingTime = cookingTime;
    }

    public boolean matches(CookingRecipeInput input, Level level) {
        return this.matches(input.getItems());
    }

    public boolean matches(NonNullList<ItemStack> input){
        NonNullList<ItemStack> inputCopy = this.copyOf(input);

        if(this.getIngredients().size() != inputCopy.size()) return false;

        for(Ingredient ingredient: this.getIngredients()){
            boolean matched = false;

            for(int i = 0; i < inputCopy.size(); i++){
                if(ingredient.test(inputCopy.get(i))){
                    inputCopy.remove(i);
                    matched = true;
                    break;
                }
            }

            if(!matched) return false;
        }
        return true;
    }

    private NonNullList<ItemStack> copyOf(NonNullList<ItemStack> stacks){
        NonNullList<ItemStack> copy = NonNullList.create();
        copy.addAll(stacks);
        return copy;
    }

    public ItemStack assemble(CookingRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.getResultStack();
    }

    public ItemStack getResultStack(){
        return this.result;
    }

    public Integer getCookingTime(){
        return this.cookingTime;
    }

    public NonNullList<ItemStack> getRemainingItems(CookingRecipeInput input) {
        return this.getRemainingItems(input.getItems());
    }

    public NonNullList<ItemStack> getRemainingItems(NonNullList<ItemStack> input){
        NonNullList<ItemStack> list = NonNullList.create();

        for(ItemStack stack: input){
            Item item = stack.getItem();

            if(item.hasCraftingRemainingItem()){
                list.add(item.getDefaultInstance());
            }
        }
        return list;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public abstract static class Serializer<T extends CookingRecipe> implements RecipeSerializer<T> {

        private final MapCodec<T> CODEC = RecordCodecBuilder.mapCodec(
            recipeInstance -> recipeInstance.group(
                    Codec.INT.fieldOf("cookingtime").forGetter(CookingRecipe::getCookingTime),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CookingRecipe::getResultStack),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(CookingRecipe::getIngredients)
                ).apply(recipeInstance, this.of())
        );

        public final StreamCodec<RegistryFriendlyByteBuf, T> STREAM_CODEC = StreamCodec.of(
            this::serialize, this::deserialize
        );

        @Override
        public MapCodec<T> codec() {
            return this.CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
            return this.STREAM_CODEC;
        }

        protected T deserialize(RegistryFriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            ingredients.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            Integer cookingTime = buffer.readVarInt();
            return this.of().apply(cookingTime, itemstack, ingredients);
        }

        protected void serialize(RegistryFriendlyByteBuf buffer, T recipe) {
            buffer.writeVarInt(recipe.getIngredients().size());
            recipe.getIngredients().forEach(ingredient ->
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient)
            );
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultStack());
            buffer.writeVarInt(recipe.getCookingTime());
        }

        protected abstract Function3<Integer, ItemStack, List<Ingredient>, T> of();
    }
}
