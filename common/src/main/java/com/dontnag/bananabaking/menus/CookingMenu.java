package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.CookingBlockEntity;
import com.dontnag.bananabaking.util.SingleItemContainer;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

public abstract class CookingMenu<T extends CookingBlockEntity<?, ?>> extends ContainerMenu<T>{

    private final SingleItemContainer output;
    private final ContainerData containerData;

    protected CookingMenu(MenuType menuType, int syncId, Inventory playerInventory, T blockEntity) {
        super(menuType, syncId, playerInventory, blockEntity);
        this.containerData = blockEntity.getContainerData();
        this.output = blockEntity.getOutput();
        this.addOutputInventory();
        addDataSlots(this.containerData);
    }

    public int getCookingProgress(){
        return this.containerData.get(0);
    }

    public void incrementCookingProgress(int amount){
        this.containerData.set(0, Math.min(this.getCookingProgress() + amount, this.getCookingTime()));
    }

    public int getCookingTime(){
        return this.containerData.get(1);
    }

    protected SingleItemContainer getOutput(){
        return this.output;
    }

    protected abstract void addOutputInventory();
}
