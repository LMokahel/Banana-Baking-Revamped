package com.dontnag.bananabaking.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import com.dontnag.bananabaking.BananaBaking;

import java.util.concurrent.CompletableFuture;

public class BananaRecipeProvider extends FabricRecipeProvider {
    public BananaRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
//        new CookingRecipeBuilder().save(exporter);
    }

    @Override
    public String getName() {
        return BananaBaking.MOD_ID;
    }
}
