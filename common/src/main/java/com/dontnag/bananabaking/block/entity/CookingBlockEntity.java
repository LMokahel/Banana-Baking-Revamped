package com.dontnag.bananabaking.block.entity;

import com.dontnag.bananabaking.block.BakingOvenBlock;
import com.dontnag.bananabaking.util.SingleItemContainer;
import com.dontnag.bananabaking.menus.CookingMenu;
import com.dontnag.bananabaking.recipe.CookingRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.List;

public abstract class CookingBlockEntity<T extends CookingMenu<?>, U extends CookingRecipe> extends ContainerBlockEntity<T>{

    private int cookingProgress = 0;
    public int cookingTime = 0;
    private final SingleItemContainer output = new SingleItemContainer();
    public final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int index) {
            return switch(index){
                case 0 -> CookingBlockEntity.this.cookingProgress;
                case 1 -> CookingBlockEntity.this.cookingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index){
                case 0 -> CookingBlockEntity.this.cookingProgress = value;
                case 1 -> CookingBlockEntity.this.cookingTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CookingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int size) {
        super(blockEntityType, blockPos, blockState, size);
    }

    public static <U extends CookingRecipe> void serverTick(Level level, BlockPos pos, BlockState state, CookingBlockEntity<?, U> blockEntity) {
        U recipe = blockEntity.getCurrentRecipe();
        blockEntity.tick(level, pos, state, blockEntity);
        if(recipe == null || !blockEntity.canCraft() || !blockEntity.isOutputValid(recipe.getResultStack())){
            blockEntity.setBoolean(BakingOvenBlock.LIT, false);
            blockEntity.resetCooking();
        }else{
            blockEntity.setCookingTime(recipe);
            blockEntity.setBoolean(BakingOvenBlock.LIT, true);
            if(blockEntity.isComplete()){
                blockEntity.resetCooking();
                blockEntity.craft(recipe);
            }
        }
        blockEntity.markDirty();
    }

    protected void craft(U recipe){
        ItemStack result = recipe.getResultStack().copy();
        this.consumeIngredients();
        this.handleRemainders(recipe);
        result.grow(this.getOutput().getItem().getCount());
        this.getOutput().setItem(result);
        this.dataAccess.set(1, 0);
        this.setChanged();
    }

    protected boolean isOutputValid(ItemStack result){
        return this.getOutput().is(result) && this.getOutput().canAbsorb(result) ||
            this.getOutput().isEmpty();
    }

    private void consumeIngredients(){
        for(int i = 0; i < 9; i++){
            this.getContainer().removeItem(i, 1);
        }
    }

    private void handleRemainders(U recipe){
        recipe.getRemainingItems(this.getInput()).forEach(this::dropItem);
    }

    protected NonNullList<ItemStack> getInput(){
        NonNullList<ItemStack> input = this.getContainer().getItems();
        NonNullList<ItemStack> list = NonNullList.create();
        for(ItemStack stack: input){
            if(!stack.isEmpty()){
                list.add(stack);
            }
        }
        return list;
    }

    private void dropItem(ItemStack stack){
        BlockPos pos = this.getBlockPos();
        Containers.dropItemStack(
            this.level,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            stack.copy()
        );
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    protected boolean isComplete(){
        return this.dataAccess.get(0) >= this.dataAccess.get(1);
    }

    public void incrementProgress(int amount){
        if(this.dataAccess.get(0) < this.dataAccess.get(1)){
            this.dataAccess.set(0, this.dataAccess.get(0) + amount);
            this.setChanged();
        }
    }

    protected void setCookingTime(U recipe){
        int cookingTime = recipe.getCookingTime();
        if(this.dataAccess.get(1) != cookingTime){
            this.dataAccess.set(1, cookingTime);
        }
    }

    protected void resetCooking(){
        if(this.dataAccess.get(0) != 0){
            this.dataAccess.set(0, 0);
        }
        this.setChanged();
    }

    public SingleItemContainer getOutput(){
        return this.output;
    }

    protected void setBoolean(BooleanProperty property, boolean val){
        if(this.getBlockState().getValue(property) != val){
            this.level.setBlock(
                this.worldPosition,
                level.getBlockState(this.worldPosition)
                    .setValue(property, val), 2
            );
            this.setChanged();
        }
    }

    protected U getCurrentRecipe(){
        RecipeManager recipeManager = this.level.getRecipeManager();
        List<RecipeHolder<U>> recipeHolders = recipeManager.getAllRecipesFor(this.getRecipeType());
        for(RecipeHolder<U> holder: recipeHolders){
            U recipe = holder.value();
            if(recipe.matches(this.getInput())){
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        this.dataAccess.set(0, tag.getInt("CookTime"));
        this.dataAccess.set(1, tag.getInt("CookTimeTotal"));
        this.getContainer().deserialize(tag.getCompound("Container"), provider);
        this.getOutput().deserialize(tag.getCompound("Output"), provider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("CookTime", this.dataAccess.get(0));
        tag.putInt("CookTimeTotal", this.dataAccess.get(1));
        tag.put("Container", this.getContainer().serialize(provider));
        tag.put("Output", this.getOutput().serialize(provider));
    }

    protected abstract void tick(Level level, BlockPos pos, BlockState state, CookingBlockEntity<?, ?> blockEntity);
    protected abstract RecipeType<U> getRecipeType();
    protected abstract boolean canCraft();
}
