package com.dontnag.bananabaking.util;

import java.awt.Point;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ToDoubleFunction;

public class Dot {

    private final int index;
    private final float increment;
    private final Point location = new Point();
    private final Point origin = new Point();

    private boolean active;

    public Dot(){
        this(-1, 0);
    }

    public Dot(int index, float increment){
        this.index = index;
        this.increment = increment;
        this.active = false;
    }

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public Point getOrigin(){
        return this.origin;
    }

    public void setOrigin(int x, int y){
        Point newOrigin = new Point(x, y);
        if(!this.getOrigin().equals(newOrigin)){
            this.setLocation(
                computeAxis(Point::getX, Math::cos),
                computeAxis(Point::getY, Math::sin)
            );
            this.getOrigin().move(x, y);
        }
    }

    public int getIndex(){
        return this.index;
    }

    public boolean contains(int mouseX, int mouseY){
        return Math.hypot(
            mouseX - this.getLocation().getX(),
            mouseY - this.getLocation().getY()
        ) <= 8;
    }

    public Point getLocation(){
        return this.location;
    }

    public void setLocation(int x, int y){
        this.getLocation().move(x, y);
    }

    private int computeAxis(ToDoubleFunction<Point> getAxis, DoubleUnaryOperator trig){
        int radius = 25;
        double coord = getAxis.applyAsDouble(this.getOrigin());
        double waveCoord = computeWaveFunc(trig);
        return (int) (coord + radius * waveCoord);
    }

    private double computeWaveFunc(DoubleUnaryOperator trig){
        double segmentLength = Math.toRadians(this.increment * this.index);
        return trig.applyAsDouble(segmentLength);
    }

    public static class Pair {

        private Dot dot;
        private long time;

        public Dot getDot(){
            return this.dot;
        }

        public void setDot(Dot dot){
            this.dot = dot;
        }

        public long getTime(){
            return this.time;
        }

        public void setTime(long time){
            this.time = time;
        }

        public void clear(){
            this.setDot(new Dot());
            this.setTime(0);
        }
    }
}
