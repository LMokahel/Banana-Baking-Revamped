package com.dontnag.bananabaking.fabric.datagen;

import com.dontnag.bananabaking.recipe.CookingRecipe;
import com.mojang.datafixers.util.Function3;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CookingRecipeBuilder implements RecipeBuilder {

    private final int cookingTime;
    private final Item result;
    private final int count;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Function3<Integer, ItemStack, List<Ingredient>, ?> constructor;


    public CookingRecipeBuilder(Item result, int count, int cookingTime, Function3<Integer, ItemStack, List<Ingredient>, ?> constructor) {
        this.result = result.asItem();
        this.count = count;
        this.cookingTime = cookingTime;
        this.constructor = constructor;
    }

    private ItemStack getResultStack(){
        return new ItemStack(this.result, this.count);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return null;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return null;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        Advancement.Builder builder = recipeOutput.advancement();
        recipeOutput.accept(id, (CookingRecipe) constructor.apply(this.cookingTime, this.getResultStack(), this.ingredients), builder.build(id.withPrefix("recipes/")));
    }
}
