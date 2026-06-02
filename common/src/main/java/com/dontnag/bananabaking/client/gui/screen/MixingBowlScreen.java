package com.dontnag.bananabaking.client.gui.screen;

import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.util.Dot;
import com.dontnag.bananabaking.util.Dot.Pair;
import com.dontnag.bananabaking.menus.MixingBowlMenu;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;

public class MixingBowlScreen extends CookingScreen<MixingBowlMenu>{

    private static final int ARROW_WIDTH = 24;
    private static final int LOOP_LENGTH = 51;
    private static final int RADIUS = LOOP_LENGTH / 2;

    private final Dot[] dots = new Dot[100];
    private final Pair start = new Pair();
    private final Pair current = new Pair();

    private float ticksActive = 0;

    public MixingBowlScreen(MixingBowlMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        float increment = 360f / this.dots.length;
        for(int i = 0; i < this.dots.length; i++){
            this.dots[i] = new Dot(i, increment);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        int xOffset = this.xSpace + 20 + RADIUS;
        int yOffset = this.ySpace + 16 + RADIUS;

        this.renderLoop(guiGraphics, partialTick, xOffset, yOffset);

        for(Dot dot: this.dots){
            dot.setOrigin(xOffset, yOffset);
            if(dot.contains(mouseX, mouseY)){
                this.onDotTouched(dot);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderProgressArrow(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        guiGraphics.blit(this.getTexture(), x + 111, y + 39, 176, 19, this.getScaledProgress(), 7);
    }

    @Override
    protected ResourceLocation getTexture() {
        return BananaBaking.id("textures/gui/mixing_bowl.png");
    }

    private void renderLoop(GuiGraphics guiGraphics, float partialTick, int xOffset, int yOffset){
        int rotateSpeed = 9;
        PoseStack pose = guiGraphics.pose();

        if(this.inUse()){
            this.ticksActive += partialTick * rotateSpeed;
        }

        pose.pushPose();
        pose.translate(xOffset, yOffset, 0.0f);
        pose.mulPose(Axis.ZN.rotationDegrees(this.ticksActive % 360));
        pose.translate(-RADIUS, -RADIUS, 0.0f);
        guiGraphics.blit(this.getTexture(), 0, 0, 177, 26, LOOP_LENGTH, LOOP_LENGTH);
        pose.popPose();
    }

    private void onDotTouched(Dot dot){
        long time = System.currentTimeMillis();

        if(!dot.isActive()){
            if(this.activeDotCount() == 0){
                this.start.setDot(dot);
            }
            dot.setActive(true);
        }else if(this.loopConcluded(dot)) {
            long perfectTime = 2000L;
            long timeElapsed = time - this.start.getTime();
            float offset = (perfectTime - timeElapsed) / 1000f;
            float penalty = Math.round(Math.max(0, offset)) * 20f;

            this.menu.incrementCookingProgress(this.activeDotCount() - (int) penalty);
            this.reset();
        }

        this.current.setTime(time);
    }

    private int activeDotCount(){
        int sum = 0;
        for(Dot dot: this.dots){
            if(dot.isActive()){
                sum++;
            }
        }
        return sum;
    }

    private void reset(){
        this.start.clear();
        this.current.clear();
        for(Dot dot: this.dots){
            dot.setActive(false);
        }
    }

    private boolean loopConcluded(Dot dot){
        int amountThreshold = 60;
        return dot.getIndex() == this.start.getDot().getIndex() &&
            this.activeDotCount() >= amountThreshold;
    }

    private boolean inUse(){
        if(this.current.getTime() == 0) return false;
        return System.currentTimeMillis() - this.current.getTime() <= 2000;
    }

    private int getScaledProgress(){
        return (int) (this.menu.getCookingProgress() /
            ((float) this.menu.getCookingTime() / ARROW_WIDTH)
        );
    }
}
