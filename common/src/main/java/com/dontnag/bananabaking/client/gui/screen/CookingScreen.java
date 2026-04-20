package com.dontnag.bananabaking.client.gui.screen;

import com.dontnag.bananabaking.menus.CookingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class CookingScreen<T extends CookingMenu<?>> extends ContainerScreen<T>{

    public CookingScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderProgressArrow(guiGraphics, this.spaceX, this.spaceY);
    }

    protected abstract void renderProgressArrow(GuiGraphics guiGraphics, int x, int y);
}
