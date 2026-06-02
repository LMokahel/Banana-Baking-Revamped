package com.dontnag.bananabaking.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CookingRecipeInput implements RecipeInput {

    NonNullList<ItemStack> items;

    public CookingRecipeInput(NonNullList<ItemStack> items){
        this.items = items;
    }

    public NonNullList<ItemStack> getItems(){
        NonNullList<ItemStack> list = NonNullList.create();
        for(ItemStack stack: this.items){
            if(!stack.isEmpty()){
                list.add(stack);
            }
        }
        return list;
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
