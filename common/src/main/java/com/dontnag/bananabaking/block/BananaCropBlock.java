package com.dontnag.bananabaking.block;

import com.dontnag.bananabaking.item.BananaItems;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BananaCropBlock extends CropBlock {

    public static final MapCodec<BananaCropBlock> CODEC = simpleCodec(BananaCropBlock::new);

    public BananaCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        return level.getBlockState(pos.above(2)).is(BlockTags.LEAVES) &&
            (above.is(Blocks.AIR) || above.is(this));
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return BananaItems.BANANA_SEEDS;
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }
}
