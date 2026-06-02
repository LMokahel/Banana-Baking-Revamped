package com.dontnag.bananabaking.block.entity;

import com.dontnag.bananabaking.block.BakingOvenBlock;
import com.dontnag.bananabaking.recipe.BakingRecipe;
import com.dontnag.bananabaking.recipe.BananaRecipeTypes;
import com.dontnag.bananabaking.menus.BakingOvenMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BakingOvenEntity extends CookingBlockEntity<BakingOvenMenu, BakingRecipe> {

    public BakingOvenEntity(BlockPos pos, BlockState blockState) {
        super(BananaBlockEntities.BAKING_OVEN.value(), pos, blockState, 9);
    }

    @Override
    protected RecipeType<BakingRecipe> getRecipeType(){
        return BananaRecipeTypes.BAKING_RECIPE.type();
    }

    @Override
    protected boolean canCraft(){
        BlockState floorState = this.level.getBlockState(this.worldPosition.below());
        return BakingOvenBlock.HEAT_PROVIDERS.contains(floorState.getBlock());
    }

    @Override
    protected BakingOvenMenu of(int syncId, Inventory playerInventory) {
        return new BakingOvenMenu(syncId, playerInventory, this);
    }

    @Override
    protected void tick(Level level, BlockPos pos, BlockState state, CookingBlockEntity<?, ?> blockEntity) {
        BlockState floor = level.getBlockState(pos.below());
        this.setBoolean(BakingOvenBlock.SOUL, floor.is(BlockTags.PIGLIN_REPELLENTS));
        if(state.getValue(BakingOvenBlock.LIT)){
            blockEntity.incrementProgress(1);
        }
    }

    @Override
    protected String getId() {
        return "baking_oven";
    }
}
