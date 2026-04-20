package com.dontnag.bananabaking.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public class SingleItemContainer implements Container {

    private ItemStack item;

    public SingleItemContainer(){
        this.item = ItemStack.EMPTY;
    }

    @NotNull
    public ItemStack getItem(){
        return this.item;
    }

    public void setItem(ItemStack stack){
        this.setChanged();
        this.item = stack;
    }

    @NotNull
    public ItemStack pop(int amount){
        this.setChanged();
        return this.getItem().split(amount);
    }

    public boolean is(ItemStack stack){
        return this.is(stack.getItem());
    }

    public boolean is(Item item){
        return this.getItem().is(item);
    }

    public int getCount(){
        return this.getItem().getCount();
    }

    public boolean canAbsorb(ItemStack stack){
        return this.getCount() + stack.getCount() <= this.getItem().getMaxStackSize();
    }

    public void deserialize(CompoundTag tag, HolderLookup.Provider provider) {
        this.setItem(tag.get("item") == null ? ItemStack.EMPTY :
            ItemStack.parseOptional(provider, tag));
    }

    @NotNull
    public Tag serialize(HolderLookup.Provider provider) {
        return this.isEmpty() ? new CompoundTag() : this.getItem().save(provider);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ItemStack stack)) return false;
        return ItemStack.isSameItemSameComponents(stack, this.getItem());
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem().isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        this.setItem(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.getItem();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.pop(amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.pop(this.getItem().getCount());
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.setItem(stack);
    }

    @Override
    public void setChanged() {}
}
