package com.dontnag.bananabaking.recipe;

import com.mojang.datafixers.util.Function3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class MixingRecipe extends CookingRecipe {

    public MixingRecipe(Integer cookingTime, ItemStack result, List<Ingredient> ingredients) {
        super(cookingTime, result, ingredients);
    }

    public static class Serializer extends CookingRecipe.Serializer<MixingRecipe>{

        @Override
        protected Function3<Integer, ItemStack, List<Ingredient>, MixingRecipe> of() {
            return MixingRecipe::new;
        }
    }
}
