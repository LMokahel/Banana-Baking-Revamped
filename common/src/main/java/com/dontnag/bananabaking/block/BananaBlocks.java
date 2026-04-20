package com.dontnag.bananabaking.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class BananaBlocks {

    public static DeferredBlock BANANA_CAKE;
    public static DeferredBlock BANANA_CROP;
    public static DeferredBlock BAKING_OVEN;
    public static DeferredBlock MIXING_BOWL;
    public static DeferredBlock VANILLA_CROP;

    private static BalmBlockRegistrar registrar;

    public static void initialize(BalmBlockRegistrar blocks) {
        registrar = blocks;
        BANANA_CAKE = register("banana_cake", BananaCakeBlock::new, Blocks.CAKE.properties());
        BANANA_CROP = register("banana_crop", BananaCropBlock::new, Blocks.WHEAT.properties()
            .noOcclusion());
        BAKING_OVEN = register("baking_oven", BakingOvenBlock::new, Blocks.BRICKS.properties()
            .lightLevel(state -> state.getValue(BakingOvenBlock.LIT) ? 10 : 0));
        VANILLA_CROP = register("vanilla_crop", VanillaCropBlock::new, Blocks.WHEAT.properties()
            .noOcclusion());
        MIXING_BOWL = register("mixing_bowl", MixingBowlBlock::new, Blocks.GLASS.properties());
    }

    public static DeferredBlock register(String name, Function<BlockBehaviour.Properties, Block> constructor, BlockBehaviour.Properties properties){
        return registrar.register(name, constructor, properties)
            .withDefaultItem()
            .asDeferredBlock();
    }
}
