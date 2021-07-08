package com.example.color.bean;

/**
 * @author Zoello
 * @description:
 * @date : 2021/7/6 17:10
 */
public class colors {
    private int color;
    private int[] xy;

    public colors(int color, int[] xy) {
        this.color = color;
        this.xy = xy;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getXy() {
        return xy;
    }

    public void setXy(int[] xy) {
        this.xy = xy;
    }
}
