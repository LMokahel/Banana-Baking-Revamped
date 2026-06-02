package com.dontnag.bananabaking.recipe;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class BakingRecipe extends CookingRecipe {

    public BakingRecipe(Integer cookingTime, ItemStack result, List<Ingredient> ingredients) {
        super(cookingTime, result, ingredients);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BananaRecipeTypes.BAKING_RECIPE.serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return BananaRecipeTypes.BAKING_RECIPE.type();
    }

    public static class Serializer extends CookingRecipe.Serializer<BakingRecipe>{

        @Override
        protected Function3<Integer, ItemStack, List<Ingredient>, BakingRecipe> of() {
            return BakingRecipe::new;
        }
    }
}
