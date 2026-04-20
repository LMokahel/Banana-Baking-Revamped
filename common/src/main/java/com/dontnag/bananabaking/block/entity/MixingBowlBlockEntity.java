package com.dontnag.bananabaking.block.entity;

import com.dontnag.bananabaking.menus.MixingBowlMenu;
import com.dontnag.bananabaking.recipe.BananaRecipeTypes;
import com.dontnag.bananabaking.recipe.MixingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MixingBowlBlockEntity extends CookingBlockEntity<MixingBowlMenu, MixingRecipe>{

    public MixingBowlBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BananaBlockEntities.MIXING_BOWL.value(), blockPos, blockState, 1);
    }

    @Override
    protected RecipeType<MixingRecipe> getRecipeType() {
        return BananaRecipeTypes.MIXING_RECIPE.type();
    }

    @Override
    protected boolean canCraft() {
        return true;
    }

    @Override
    protected MixingBowlMenu of(int syncId, Inventory playerInventory) {
        return new MixingBowlMenu(syncId, playerInventory, this);
    }

    @Override
    protected String getId() {
        return "mixing_bowl";
    }

    @Override
    protected void tick(Level level, BlockPos pos, BlockState state, CookingBlockEntity<?, ?> blockEntity) {

    }
}
