package com.dontnag.bananabaking.recipe;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BakingRecipe extends CookingRecipe {

    public BakingRecipe(Integer cookingTime, ItemStack result, List<Ingredient> ingredients) {
        super(cookingTime, result, ingredients);
    }

    public static class Serializer extends CookingRecipe.Serializer<BakingRecipe>{

        @Override
        protected Function3<Integer, ItemStack, List<Ingredient>, BakingRecipe> of() {
            return BakingRecipe::new;
        }
    }
}
