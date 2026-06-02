package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.CookingBlockEntity;
import com.dontnag.bananabaking.util.SingleItemContainer;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

public abstract class CookingMenu<T extends CookingBlockEntity<?, ?>> extends ContainerMenu<T>{

    private final SingleItemContainer output;
    private final T blockEntity;

    protected CookingMenu(MenuType menuType, int syncId, Inventory playerInventory, T blockEntity) {
        super(menuType, syncId, playerInventory, blockEntity);
        this.blockEntity = blockEntity;
        this.output = blockEntity.getOutput();
        this.addOutputInventory();
        addDataSlots(blockEntity.getContainerData());
    }

    public int getCookingProgress(){
        return blockEntity.dataAccess.get(0);
    }

    public void incrementCookingProgress(int amount){
        blockEntity.dataAccess.set(0, Math.min(this.getCookingProgress() + amount, this.getCookingTime()));
    }

    public int getCookingTime(){
        return blockEntity.dataAccess.get(1);
    }

    public void setCookingTime(int cookingTime){
        blockEntity.dataAccess.set(1, cookingTime);
    }

    protected SingleItemContainer getOutput(){
        return this.output;
    }

    protected abstract void addOutputInventory();
}
