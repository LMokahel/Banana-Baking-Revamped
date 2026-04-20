package com.dontnag.bananabaking.fabric.datagen;

import com.dontnag.bananabaking.block.BananaBlocks;
import com.dontnag.bananabaking.item.BananaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.concurrent.CompletableFuture;

public class BananaBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected BananaBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(dataOutput, provider);
    }

    @Override
    public void generate() {
        LootItemCondition.Builder condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEETROOTS)
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE));
        this.add(BananaBlocks.BANANA_CROP.asBlock(), this.createCropDrops(BananaBlocks.BANANA_CROP.asBlock(), BananaItems.BANANA.asItem(), BananaItems.BANANA_SEEDS.asItem(), condition));
        this.add(BananaBlocks.VANILLA_CROP.asBlock(), this.createCropDrops(BananaBlocks.VANILLA_CROP.asBlock(), BananaItems.VANILLA_BEANS.asItem(), BananaItems.VANILLA_BEAN_SEEDS.asItem(), condition));
        this.dropSelf(BananaBlocks.BAKING_OVEN.asBlock());
        this.add(BananaBlocks.BANANA_CAKE.asBlock(), noDrop());
    }
}
