package com.dontnag.bananabaking.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CookingRecipeInput implements RecipeInput {

    NonNullList<ItemStack> items;

    public CookingRecipeInput(NonNullList<ItemStack> items){
        this.items = items;
    }

    public NonNullList<ItemStack> getItems(){
        return this.items.stream()
            .filter(Predicate.not(ItemStack::isEmpty))
            .collect(Collectors.toCollection(NonNullList::create));
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public int size() {
        return this.items.size();
    }
}
