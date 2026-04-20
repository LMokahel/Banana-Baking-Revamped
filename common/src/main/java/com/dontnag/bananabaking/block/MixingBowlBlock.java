package com.dontnag.bananabaking.block;

import com.dontnag.bananabaking.block.entity.BananaBlockEntities;
import com.dontnag.bananabaking.block.entity.MixingBowlBlockEntity;
import com.dontnag.bananabaking.item.BananaItems;

import com.mojang.serialization.MapCodec;

import net.blay09.mods.balm.api.Balm;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MixingBowlBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = AbstractFurnaceBlock.LIT;
    public static final MapCodec<MixingBowlBlock> CODEC = MixingBowlBlock.simpleCodec(MixingBowlBlock::new);
    public static final Set<Block> HEAT_PROVIDERS = Set.of(
        Blocks.FIRE,
        Blocks.CAMPFIRE,
        Blocks.SOUL_CAMPFIRE,
        Blocks.SOUL_FIRE
    );

    public MixingBowlBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(LIT, Boolean.FALSE)
        );
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(
            box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
            box(2.0, 2.0, 2.0, 14.0, 13.0, 14.0),
            BooleanOp.ONLY_FIRST
        );
    }

    @Override
    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_) {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_) {
        return true;
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
    protected @NotNull MapCodec<MixingBowlBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(BananaItems.MIXING_SPOON.asItem())){
            MixingBowlBlockEntity blockEntity = (MixingBowlBlockEntity) level.getBlockEntity(pos);
            if (!level.isClientSide) {
                Balm.getNetworking().openMenu(player, blockEntity);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixingBowlBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BananaBlockEntities.MIXING_BOWL.value(), MixingBowlBlockEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
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
