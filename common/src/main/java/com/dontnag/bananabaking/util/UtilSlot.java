package com.dontnag.bananabaking.util;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class UtilSlot extends Slot {

    private final Container swapContainer;

    public UtilSlot(Container container, Container swapContainer, int slot, int x, int y){
        super(container, slot, x, y);
        this.swapContainer = swapContainer;
    }

    public boolean is(Slot slot){
        return ItemStack.isSameItem(this.getItem(), slot.getItem());
    }

    public boolean canTransfer(UtilSlot slot){
        return this.is(slot) && !slot.isFull() || slot.isEmpty();
    }

    public int getSpace(){
        return this.isEmpty() ? 64 : this.getMaxSize() - this.getCount();
    }

    public boolean isFull(){
        return this.getSpace() == 0;
    }

    public int getMaxSize(){
        return this.getItem().getMaxStackSize();
    }

    public boolean isEmpty(){
        return !super.hasItem();
    }

    public int getCount(){
        return this.getItem().getCount();
    }

    public Container getSwapContainer(){
        return this.swapContainer;
    }

    public Container getContainer(){
        return this.container;
    }

    public void transfer(UtilSlot target){
        int amount = Math.min(this.getCount(), target.getSpace());
        target.set(this.getItem().copyWithCount(target.getCount() + amount));
        this.set(this.getItem().copyWithCount(this.getCount() - amount));
    }
}
