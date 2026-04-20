package com.dontnag.bananabaking.client.gui.screen;

import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.util.MixingDot;
import com.dontnag.bananabaking.menus.MixingBowlMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MixingBowlScreen extends CookingScreen<MixingBowlMenu>{

    public static final int DOT_AMOUNT = 100;
    private static final int ARROW_WIDTH = 24;
    private static final int DECORATOR_HEIGHT = 16;
    private static final ResourceLocation GUI = BananaBaking.id("textures/gui/mixing_bowl.png");
    private static final ResourceLocation ARROW = BananaBaking.id("textures/gui/arrow.png");
    private final MixingDot[] dots = new MixingDot[DOT_AMOUNT];
    private final List<MixingDot> activeDots = new ArrayList<>();
    private final List<Long> times = new ArrayList<>();
    private int lastIndex;

    public MixingBowlScreen(MixingBowlMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        for(int i = 0; i < DOT_AMOUNT; i++){
            this.dots[i] = new MixingDot(i);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        for(MixingDot dot: this.dots){
            dot.setX(this.spaceX + this.imageWidth / 4);
            dot.setY(this.spaceY + this.imageHeight / 4);
            if(dot.contains(mouseX, mouseY)){
                this.onDotTouched(dot);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        guiGraphics.blit(ARROW, this.spaceX + this.imageWidth / 4, this.spaceY + this.imageHeight / 4, 0, 0, 50, 50);
    }

    @Override
    protected void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(this.getTexture(), x + 85, y + 34, 177, 13, this.getScaledProgress(), DECORATOR_HEIGHT);
    }

    @Override
    protected ResourceLocation getTexture() {
        return GUI;
    }

    private void onDotTouched(MixingDot dot){
        if(this.activeDots.isEmpty()){
            this.lastIndex = dot.getIndex();
        }else if(dot.getIndex() == this.lastIndex && this.activeDots.size() >= 60){
            this.menu.incrementCookingProgress(this.activeDots.size());
            this.activeDots.clear();
        }
        long time = System.currentTimeMillis();
        if(times.isEmpty() || time != times.getLast()){
            times.add(time - times.getLast());
        }
        this.activeDots.add(dot);
    }

    private int getScaledProgress(){
        return (int) (this.menu.getCookingProgress() / ((float) this.menu.getCookingTime() / ARROW_WIDTH));
    }
}
