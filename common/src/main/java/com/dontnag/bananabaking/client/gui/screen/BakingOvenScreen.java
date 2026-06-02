package com.dontnag.bananabaking.client.gui.screen;

import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.block.BakingOvenBlock;
import com.dontnag.bananabaking.block.entity.ContainerBlockEntity;
import com.dontnag.bananabaking.menus.BakingOvenMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class BakingOvenScreen extends CookingScreen<BakingOvenMenu> {

    private static final int ARROW_WIDTH = 23;
    private static final int FIRE_WIDTH = 11;
    private static final int DECORATOR_HEIGHT = 16;

    public BakingOvenScreen(BakingOvenMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void renderProgressArrow(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        guiGraphics.blit(this.getTexture(), x + 85, y + 34, 177, 13, this.getScaledProgress(), DECORATOR_HEIGHT);
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(BananaBaking.MOD_ID, "textures/gui/baking_oven.png");
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        this.renderFire(guiGraphics, this.xSpace, this.ySpace);
    }

    private void renderFire(GuiGraphics guiGraphics, int x, int y){
        ContainerBlockEntity<?> blockEntity = this.menu.getBlockEntity();
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        BakingOvenBlock oven = (BakingOvenBlock) level.getBlockState(pos).getBlock();
        if(oven.isHeated(level, pos)){
            int yOffset = 30 + (oven.hasSouls(level, pos) ? 0 : 16);
            guiGraphics.blit(this.getTexture(), x + 91, y + 53, 176, yOffset, FIRE_WIDTH, DECORATOR_HEIGHT);
        }
    }

    private int getScaledProgress(){
        return (int) (this.menu.getCookingProgress() / ((float) this.menu.getCookingTime() / ARROW_WIDTH));
    }
}
