package com.litchiny.customviews.model;

/**
 * author: LL .
 * date:   On 2019/1/23
 * caption:
 */

public class Point {
    public float endY;
    public float endX;
    public int realIndex = -1;    //实际的index位置
    public int startIndex = -1;   //初始的index的位置
    public boolean isEmpty = true;
    public boolean isLongClick = false;
    public int level = -1;       //颜色等级
    public int[] areaArrayIndex = new int[]{-1,-1,-1,-1};
    public boolean isAllowChange = true;
    public Point(float endX, float endY) {
        this(endX, endY, -1, -1, true);
    }

    public Point(float endX, float endY, int realIndex, int startIndex, boolean isEmpty,int level) {
        this.endY = endY;
        this.endX = endX;
        this.realIndex = realIndex;
        this.startIndex = startIndex;
        this.isEmpty = isEmpty;
        this.level = level;
    }

    public Point(float endX, float endY, int realIndex, int startIndex, boolean isEmpty) {
        this(endX, endY, realIndex, startIndex, isEmpty,-1);
    }

    @Override
    public String toString() {
        return "Point{" +
//                "endX=" + endX +
//                "endY=" + endY +
                "level=" + level +
                ", realIndex=" + realIndex +
                ", startIndex=" + startIndex +
                ", isEmpty=" + isEmpty +
                ", isAllowChange=" + isAllowChange +
                '}';
    }
}
