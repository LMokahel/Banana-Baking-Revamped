package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.BakingOvenEntity;
import com.dontnag.bananabaking.util.UtilSlot;

import net.minecraft.world.entity.player.Inventory;

public class BakingOvenMenu extends CookingMenu<BakingOvenEntity> {

    public BakingOvenMenu(int syncId, Inventory playerInventory, BakingOvenEntity blockEntity) {
        super(BananaMenuTypes.BAKING_MENU.value(), syncId, playerInventory, blockEntity);
    }

    @Override
    protected void addContainerInventory(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                this.addSlot(new UtilSlot(this.getContainer(), this.getPlayerContainer(), i * 3 + j, 20 + (18 * j), 16 + (18 * i)));
            }
        }
    }

    @Override
    protected void addOutputInventory() {
        this.addSlot(new UtilSlot(this.getOutput(), this.getPlayerContainer(), 0, 126, 35));
    }
}


