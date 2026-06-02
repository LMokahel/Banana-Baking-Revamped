package com.dontnag.bananabaking.client.gui.screen;

import com.dontnag.bananabaking.menus.ContainerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class ContainerScreen<T extends ContainerMenu<?>> extends AbstractContainerScreen<T> {

    public int xSpace;
    public int ySpace;

    public ContainerScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.getTexture());
        this.xSpace = (width - imageWidth) / 2;
        this.ySpace = (height - imageHeight) / 2;
        guiGraphics.blit(this.getTexture(), this.xSpace, this.ySpace, 0, 0, imageWidth, imageHeight);
    }

    protected abstract ResourceLocation getTexture();
}
