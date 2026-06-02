package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.MixingBowlBlockEntity;
import com.dontnag.bananabaking.util.UtilSlot;

import net.minecraft.world.entity.player.Inventory;

public class MixingBowlMenu extends CookingMenu<MixingBowlBlockEntity>{




    public MixingBowlMenu(int syncId, Inventory playerInventory, MixingBowlBlockEntity blockEntity) {
        super(BananaMenuTypes.MIXING_MENU.value(), syncId, playerInventory, blockEntity);
    }

    @Override
    protected void addContainerInventory() {
        this.addSlot(new UtilSlot(this.getContainer(), this.getPlayerContainer(), 0, 85, 35));
    }

    @Override
    protected void addOutputInventory() {
        this.addSlot(new UtilSlot(this.getOutput(), this.getPlayerContainer(), 0, 148, 35));
    }
}
