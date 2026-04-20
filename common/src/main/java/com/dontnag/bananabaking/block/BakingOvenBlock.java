package com.dontnag.bananabaking.block;

import com.dontnag.bananabaking.block.entity.BananaBlockEntities;
import com.dontnag.bananabaking.block.entity.BakingOvenEntity;

import com.mojang.serialization.MapCodec;

import net.blay09.mods.balm.api.Balm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class BakingOvenBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;
    public static final BooleanProperty SOUL = BooleanProperty.create("soul");
    public static final MapCodec<BakingOvenBlock> CODEC = BakingOvenBlock.simpleCodec(BakingOvenBlock::new);
    public static final Set<Block> HEAT_PROVIDERS = Set.of(
        Blocks.FIRE,
        Blocks.CAMPFIRE,
        Blocks.SOUL_CAMPFIRE,
        Blocks.SOUL_FIRE
    );

    public BakingOvenBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(LIT, Boolean.FALSE)
            .setValue(SOUL, Boolean.FALSE)
        );
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof AbstractFurnaceBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, (AbstractFurnaceBlockEntity)blockentity);
                    ((AbstractFurnaceBlockEntity)blockentity).getRecipesToAwardAndPopExperience((ServerLevel)level, Vec3.atCenterOf(pos));
                }

                super.onRemove(state, level, pos, newState, isMoving);
                level.updateNeighbourForOutputSignal(pos, this);
            } else {
                super.onRemove(state, level, pos, newState, isMoving);
            }
        }
    }

    @Override
    protected @NotNull MapCodec<BakingOvenBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BakingOvenEntity blockEntity = (BakingOvenEntity) level.getBlockEntity(pos);
        if (!level.isClientSide) {
            Balm.getNetworking().openMenu(player, blockEntity);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BakingOvenEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BananaBlockEntities.BAKING_OVEN.value(), BakingOvenEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, SOUL);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING);
        Direction.Axis axis = direction.getAxis();
        double horizontalOffset = random.nextDouble() * 0.6 - 0.3;
        double xOffset = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : horizontalOffset;
        double yOffset = random.nextDouble() * 6.0 / 12.0;
        double zOffset = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : horizontalOffset;
        if(isHeated(level, pos)){
            for(float offset = 0; offset < 4; offset++){
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + ((offset / 10) + 1) + yOffset, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            }
        }
        if(state.getValue(BakingOvenBlock.LIT)){
            level.addParticle(
                hasSouls(level, pos) ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME,
                pos.getX() + 0.5 + xOffset,
                pos.getY() + 0.25 + yOffset,
                pos.getZ() + 0.5 + zOffset,
                0.0, 0.0, 0.0
            );
        }
    }

    public boolean hasSouls(Level level, BlockPos pos){
        BlockState state = level.getBlockState(pos);
        return state.getValue(BakingOvenBlock.SOUL);
    }

    public boolean isHeated(Level level, BlockPos pos){
        BlockState floor = level.getBlockState(pos.below());
        if(floor.is(BlockTags.CAMPFIRES)){
            if(floor.hasProperty(CampfireBlock.LIT)){
                return HEAT_PROVIDERS.contains(floor.getBlock()) && floor.getValue(CampfireBlock.LIT);
            }
            return false;
        }
        return HEAT_PROVIDERS.contains(floor.getBlock());
    }
}
