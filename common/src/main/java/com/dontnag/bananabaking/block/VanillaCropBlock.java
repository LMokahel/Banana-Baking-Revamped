package com.dontnag.bananabaking.block;

import com.dontnag.bananabaking.item.BananaItems;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VanillaCropBlock extends CropBlock {

    public static final MapCodec<VanillaCropBlock> CODEC = simpleCodec(VanillaCropBlock::new);
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 1);
    public static final BooleanProperty SPROUTED = BooleanProperty.create("sprouted");

    public VanillaCropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(STAGE, 0)
            .setValue(SPROUTED, false)
        );
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, this.getAge(state) * 2 + 2, 16);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(level.getLightEmission(pos) < 9) return;
        float f = level.getBlockState(pos.below()).getValue(FarmBlock.MOISTURE);
        if (random.nextInt((int)(25.0F / f) + 1) == 0) {
            this.growCrops(level, pos, state);
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        if(level.isClientSide || !this.isValidBonemealTarget(level, pos, state)) return;
        trySproutOrGrow(state, level, pos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) ||
            state.is(this) && state.getValue(SPROUTED);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return BananaItems.VANILLA_BEAN_SEEDS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, STAGE, SPROUTED);
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return this.getAge(state) < this.getMaxAge() || this.isBase(state) &&
            level.getBlockState(pos.above()).is(Blocks.AIR);
    }

    private void trySproutOrGrow(BlockState state, Level level, BlockPos pos){
        int age = this.getAge(state);
        int nextAge = age + this.getBonemealAgeIncrease(level);
        nextAge = Math.clamp(nextAge, 0, this.getMaxAge());
        if(canSprout(level, pos, state)){
            this.sproutCrop(level, pos);
        }else{
            level.setBlock(pos, state.setValue(AGE, nextAge), 2);
        }
    }

    private boolean isBase(BlockState state){
        return state.getValue(STAGE) == 0;
    }

    private boolean isSprouted(BlockState state){
        return state.getValue(SPROUTED);
    }

    private void sproutCrop(Level level, BlockPos pos){
        level.setBlock(pos, this.defaultBlockState()
            .setValue(AGE, this.getMaxAge())
            .setValue(SPROUTED, true), 2);
        level.setBlock(pos.above(), this.defaultBlockState()
            .setValue(STAGE, 1), 2);
    }

    private boolean canSprout(Level level, BlockPos pos, BlockState state){
        return level.getBlockState(pos.above()).is(Blocks.AIR) &&
            level.getBlockState(pos.below()).is(Blocks.FARMLAND) &&
            this.getAge(state) == this.getMaxAge();
    }
}
