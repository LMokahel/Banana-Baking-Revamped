package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.ContainerBlockEntity;
import com.dontnag.bananabaking.util.UtilSlot;

import net.blay09.mods.balm.api.container.DefaultContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ContainerMenu<T extends ContainerBlockEntity<?>> extends AbstractContainerMenu {

    private final Map<Container, List<UtilSlot>> utilSlots = new HashMap<>();
    private final DefaultContainer container;
    private final DefaultContainer items;
    private final T blockEntity;

    protected ContainerMenu(MenuType<?> menuType, int syncId, Inventory playerInventory, T blockEntity) {
        super(menuType, syncId);
        this.blockEntity = blockEntity;
        this.container = this.blockEntity.getContainer();
        this.items = new DefaultContainer(playerInventory.items);
        this.addPlayerHotbar();
        this.addPlayerInventory();
        this.addContainerInventory();
    }

    public T getBlockEntity(){
        return this.blockEntity;
    }

    protected Container getContainer(){
        return this.container;
    }

    protected Container getPlayerContainer(){
        return this.items;
    }

    protected void addSlot(UtilSlot slot){
        this.utilSlots.computeIfAbsent(slot.getContainer(), c -> new ArrayList<>()).add(slot);
        super.addSlot(slot);
    }

    protected UtilSlot getUtil(Container container, int index){
        return this.utilSlots.get(container).stream()
            .filter(uSlot -> index == uSlot.getContainerSlot())
            .findAny()
            .orElse(null);
    }

    private void addPlayerInventory() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new UtilSlot(this.getPlayerContainer(), this.getContainer(), j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar() {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new UtilSlot(this.getPlayerContainer(), this.getContainer(), i, 8 + i * 18, 142));
        }
    }

    private int getAvailableSlot(Container swap, UtilSlot slot){
        int size = swap.getContainerSize();
        if((swap instanceof Inventory inventory)) size = inventory.items.size();
        for(int i = 0; i < size; i++){
            if(slot.canTransfer(getUtil(swap, i))) return i;
        }
        return -1;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        UtilSlot slot = (UtilSlot) this.getSlot(index);
        if(!slot.isEmpty()){
            Container swap = slot.getSwapContainer();
            int available = getAvailableSlot(swap, slot);
            if(available != -1){
                slot.transfer(this.getUtil(swap, available));
            }
        }
        return slot.getItem();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.getContainer().stillValid(player);
    }

    protected abstract void addContainerInventory();
}
