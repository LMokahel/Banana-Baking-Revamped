package com.dontnag.bananabaking.recipe;

import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.item.crafting.DeferredRecipeType;

public class BananaRecipeTypes {

    public static DeferredRecipeType<CookingRecipeInput, BakingRecipe> BAKING_RECIPE;
    public static DeferredRecipeType<CookingRecipeInput, MixingRecipe> MIXING_RECIPE;
//    public static DeferredRecipeType<CookingRecipeInput, CookingRecipe> BLENDING_RECIPE;
//    public static DeferredRecipeType<CookingRecipeInput, CookingRecipe> FRYING_RECIPE;
//    public static DeferredRecipeType<CookingRecipeInput, CookingRecipe> FREEZING_RECIPE;
//    public static DeferredRecipeType<CookingRecipeInput, CookingRecipe> PRESSING_RECIPE;

    public static void initialize(BalmRecipeTypeRegistrar recipeTypes){
        BAKING_RECIPE = recipeTypes.register("baking", BakingRecipe.class)
            .withSerializer(BakingRecipe.Serializer::new)
            .asDeferredRecipeType();
        MIXING_RECIPE = recipeTypes.register("mixing", MixingRecipe.class)
            .withSerializer(MixingRecipe.Serializer::new)
            .asDeferredRecipeType();
//        BLENDING_RECIPE = recipeTypes.register("blending", CookingRecipe.class)
//            .withSerializer(CookingRecipe.Serializer::new)
//            .asDeferredRecipeType();
//        FRYING_RECIPE = recipeTypes.register("frying", CookingRecipe.class)
//            .withSerializer(CookingRecipe.Serializer::new)
//            .asDeferredRecipeType();
//        FREEZING_RECIPE = recipeTypes.register("freezing", CookingRecipe.class)
//            .withSerializer(CookingRecipe.Serializer::new)
//            .asDeferredRecipeType();
//        PRESSING_RECIPE = recipeTypes.register("pressing", CookingRecipe.class)
//            .withSerializer(CookingRecipe.Serializer::new)
//            .asDeferredRecipeType();
    }
}
