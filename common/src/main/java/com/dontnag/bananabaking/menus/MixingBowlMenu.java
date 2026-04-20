package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.MixingBowlBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class MixingBowlMenu extends CookingMenu<MixingBowlBlockEntity>{

    public MixingBowlMenu(int syncId, Inventory playerInventory, MixingBowlBlockEntity blockEntity) {
        super(BananaMenuTypes.MIXING_MENU.value(), syncId, playerInventory, blockEntity);
    }

    @Override
    protected void addOutputInventory() {

    }

    @Override
    protected void addContainerInventory() {

    }
}
