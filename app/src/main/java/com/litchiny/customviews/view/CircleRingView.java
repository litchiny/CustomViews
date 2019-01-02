package com.litchiny.customviews.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.litchiny.customviews.R;
import com.litchiny.customviews.model.CircleRingMode;
import com.litchiny.customviews.utils.AppUtil;
import com.litchiny.customviews.utils.UnitUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 一个支持圆环不同类型展示的view
 *
 * @author Litchiny
 */
public class CircleRingView extends View {
    private static final String TAG = "SleepRingView";
    private int[] mColors;
    private Paint circlePaint;                                                                      //表盘外轮廓画笔
    private Paint linePaint;                                                                       // 表盘内短线画笔
    private int mWidth;
    private int mHeight;
    private float startPercent = 0;
    private float initStartPercent;
    //圆环的画笔
    private Paint cyclePaint;
    //圆的直径
    private float mRadius = 300;
    //圆的粗细
    private float mStrokeWidth = 80;
    private List<CircleRingMode> ringModes = new ArrayList<>();
    private Context mContext;
    private int startY = 0;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();//颜色渐变插值器
    private int radius;

    public CircleRingView(Context context) {
        this(context, null);
    }

    public CircleRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mColors = new int[]{
                context.getResources().getColor(R.color.colorSleepGradient2),
                context.getResources().getColor(R.color.colorSleepGradient3),
                context.getResources().getColor(R.color.colorSleepGradient),
                context.getResources().getColor(R.color.colorSleepGradient4)
        };
        initPaint();
    }

    /**
     * 创建所有的paint
     */
    private void initPaint() {
        int strokeWidth = UnitUtil.dp2px(mContext, 2.5f);
        circlePaint = AppUtil.getAvailablePaint(Color.BLUE, strokeWidth, Paint.Style.STROKE);
        linePaint = AppUtil.getAvailablePaint(mContext.getResources().getColor(R.color.colorSleepGradient), strokeWidth, Paint.Style.FILL);
        linePaint.setTextSize(UnitUtil.dp2px(getContext(), 12));
        mStrokeWidth = UnitUtil.dp2px(mContext, 24);
        cyclePaint = AppUtil.getAvailablePaint(Color.BLUE, (int) mStrokeWidth, Paint.Style.STROKE);
        startY = UnitUtil.dp2px(mContext, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setShader(new LinearGradient(3, 3, mWidth - 3, mHeight - 3, mColors, null, Shader.TileMode.CLAMP));
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, circlePaint);
        mRadius = radius * 2 - startY * 6;
        canvas.translate(mWidth / 2 - mRadius / 2, mHeight / 2 - mRadius / 2);
        if (ringModes.size() == 0)
            return;
        //画圆环
        drawCycle(canvas);
        drawSquare(canvas);
    }

    //画圆环
    private void drawCycle(Canvas canvas) {
        float sweepPercent = 0f;
        CircleRingMode mode;
        startPercent = initStartPercent;
        for (int i = 0; i < ringModes.size(); i++) {
            mode = ringModes.get(i);
            cyclePaint.setColor(mode.getColorValue());
            startPercent += (sweepPercent - 0.5f);
            if (i < 2) {
                sweepPercent = (mode.getProgress() + 0.5f) * 360 / 100f;
                startPercent -= 1f;
            } else if (i == ringModes.size() - 1) {
                sweepPercent = 360f + initStartPercent - startPercent - 0.5f;
            } else {
                sweepPercent = mode.getProgress() * 360 / 100f;
            }
            canvas.drawArc(new RectF(0, 0, mRadius, mRadius), startPercent, sweepPercent, false, cyclePaint);
        }
    }

    private void drawSquare(Canvas canvas) {
        int base = Math.min(mWidth, mHeight) / 25;
        int startTop = mHeight / 2 - base * 3;
        int startLeft = mWidth / 2 + base;
        for (CircleRingMode mode : ringModes) {
            linePaint.setColor(mode.getColorValue());
            canvas.drawRect(new Rect(startLeft, startTop, startLeft + base, startTop + base), linePaint);
            canvas.drawText(mode.getProgress() + "%", startLeft + base + 10, startTop + base, linePaint);
            startTop += base * 2;
        }
    }

    /**
     * 获取宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mHeight = getDefaultSize(getSuggestedMinimumWidth(), heightMeasureSpec);
        radius = Math.min(mWidth / 2, mHeight / 2) - UnitUtil.dp2px(mContext, 24);
        Log.d(TAG, "onMeasure: mWidth: " + mWidth + ",mHeight: " + mHeight);
    }


    public void setData(List<CircleRingMode> modeList) {
       this.setData(modeList,0);
    }

    public void setData(List<CircleRingMode> modeList, float hour) {
        if (modeList.size() == 0)
            return;
        if (this.ringModes.size() > 0)
            this.ringModes.clear();
        this.ringModes.addAll(modeList);
        this.startPercent = this.initStartPercent = 360 * hour / 24f - 90;
        invalidate();
    }
}

