package com.litchiny.customviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.litchiny.customviews.R;
import com.litchiny.customviews.utils.AppUtil;
import com.litchiny.customviews.utils.UnitUtil;

/**
 * Icons的集合
 *
 * @author Litchiny
 */

public class IconsView extends View {
    private Paint mPaint;
    private Path mPath;
    private int startRect;
    private int baseWidth;

    public IconsView(Context context) {
        this(context, null);
    }

    public IconsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = AppUtil.getAvailablePaint(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), UnitUtil.dp2px(getContext(), 2), Paint.Style.STROKE);
        mPath = new Path();
        startRect = 100;
        baseWidth = 50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCar(canvas);
        drawShopCart(canvas);
    }

    private void drawCar(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        int startPath = startRect + baseWidth;
        canvas.drawRoundRect(new RectF(startRect, startRect, startPath, startPath), 5, 5, mPaint);
        mPath.reset();
        mPath.moveTo(startPath, startRect + baseWidth / 3);
        mPath.lineTo(startPath + baseWidth / 3, startRect + baseWidth / 3);
        mPath.lineTo(startPath + baseWidth / 2, startRect + baseWidth / 5 * 4);
        mPath.lineTo(startPath + baseWidth / 2, startRect + baseWidth);
        mPath.lineTo(startPath, startRect + baseWidth);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        int raidus = baseWidth / 5;
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startPath + raidus, startRect + baseWidth, raidus, mPaint);
        canvas.drawCircle(startRect + raidus * 1.5f, startRect + baseWidth, raidus, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        canvas.drawCircle(startPath + raidus, startRect + baseWidth, raidus, mPaint);
        canvas.drawCircle(startRect + raidus * 1.5f, startRect + baseWidth, raidus, mPaint);
    }

    private void drawShopCart(Canvas canvas) {
        int y = startRect ;
        int x = startRect + baseWidth * 3;
        mPath.reset();
        mPath.moveTo(x, y);
        mPath.lineTo(x + baseWidth / 4, y);
        mPath.lineTo(x + baseWidth / 2, y + baseWidth);
        mPath.lineTo(x + baseWidth / 4, y + baseWidth / 4 * 5);
        mPath.lineTo(x + baseWidth / 4 * 5, y + baseWidth / 4 * 5);
        canvas.drawPath(mPath, mPaint);
        mPath.reset();
        mPath.moveTo(x + baseWidth / 4 + 5, y + baseWidth / 12 * 5);
        mPath.lineTo(x + baseWidth * 5 / 4, y + baseWidth / 12 * 5);
        mPath.lineTo(x + baseWidth, y + baseWidth);
        mPath.lineTo(x + baseWidth / 2, y + baseWidth);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        int raidus = baseWidth / 8;
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x + baseWidth / 4 * 5 - raidus, y + baseWidth / 4 * 5 + raidus * 2, raidus, mPaint);
        canvas.drawCircle(x + baseWidth / 2 - raidus, y + baseWidth / 4 * 5 + raidus * 2, raidus, mPaint);
    }
}
