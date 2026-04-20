package com.dontnag.bananabaking.fabric.datagen;

import com.dontnag.bananabaking.item.BananaItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class BananaModelProvider extends FabricModelProvider {
    public BananaModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {}

    @Override
    public void generateItemModels(ItemModelGenerators modelGenerator) {
        BananaItems.ITEMS.forEach(item ->
            modelGenerator.generateFlatItem(item.asItem(), ModelTemplates.FLAT_ITEM)
        );
    }
}
