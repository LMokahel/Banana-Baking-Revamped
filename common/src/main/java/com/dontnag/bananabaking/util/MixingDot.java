package com.dontnag.bananabaking.util;

import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.client.gui.screen.MixingBowlScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class MixingDot {

    private static final int RADIUS = 25;
    private static final float INCREMENT = 360f / MixingBowlScreen.DOT_AMOUNT;
    private int x;
    private int y;
    private final int index;
    private double[] point;
    private boolean set;
    private boolean active;

    public MixingDot(int index){
        this.index = index;
        this.set = false;
        this.active = false;
    }

    public void setPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setActive(){
        this.active = true;
    }

    public boolean isActive(){
        return this.active;
    }

    public double getX(){
        if(!this.set){
            this.point = this.getPoint();
            this.set = true;
        }
        return this.point[0];
    }

    public double getY(){
        if(!this.set){
            this.point = this.getPoint();
            this.set = true;
        }
        return this.point[1];
    }

    public int getIndex(){
        return this.index;
    }

    public ResourceLocation getTexture(){
        return BananaBaking.id("textures/gui/" + (this.active ? "a" : "") + "dot.png");
    }

    public boolean contains(int mouseX, int mouseY){
        return Math.abs(Math.hypot(mouseX - this.getX(), mouseY - this.getY())) <= 3f;
    }

    private double[] getPoint(){
        return new double[]{
            this.x + RADIUS * getOffset(Math::cos),
            this.y + RADIUS * getOffset(Math::sin)
        };
    }

    private double getOffset(Function<Double, Double> func){
        return func.apply(Math.toRadians((INCREMENT * this.index)));
    }
}
