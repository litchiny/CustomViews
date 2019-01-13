package com.litchiny.customviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.litchiny.customviews.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一个流式布局view
 *
 * @author Litchiny
 */
public class FlowLayout extends View {
    private static final String TAG = "FlowLayout";
    private Paint mPaint;
    private List<String> textList = new ArrayList<>();
    private int minPadding = 100;
    private int textPadding = 20;
    private float textSize = 30;
    private int textPace = 35;                                //两个item的间隔
    private int textHeight = 40;
    private int distanceW = 1080;
    private List<int[]> pointList = new ArrayList<>();
    private int clickIndex = -1;
    private long touchDownTime;
    private boolean isLongClick;
    private int textNormalColor;
    private int textClickColor;
    private boolean isShowEndAdd;                            //最后一个字符串是否是+
    private FlowClickListener listener;
    private boolean paddingHasChanged;
    private int minHeight = 500;
    private int baseItemHeight;
    private int totalTextWidth;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        textNormalColor = a.getColor(R.styleable.FlowLayout_flow_text_normal_color, Color.BLUE);
        textClickColor = a.getColor(R.styleable.FlowLayout_flow_text_click_color, Color.WHITE);
        textSize = a.getDimension(R.styleable.FlowLayout_flow_text_size, context.getResources().getDimension(R.dimen.dimen_size_15));
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        Rect rect = new Rect();
        mPaint.getTextBounds("1", 0, "1".length(), rect);
        textHeight = rect.height();
        textPadding = textHeight;
        textPace = textHeight * 3 / 2;
        baseItemHeight = textHeight + textPadding * 2 + textPace;
        mPaint.setAntiAlias(true);
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setData(List<String> textList) {
        setData(textList, false, -1);
    }

    public void setData(List<String> textList, boolean isShowEndAdd, int clickIndex) {
        this.isShowEndAdd = isShowEndAdd;
        this.clickIndex = clickIndex;
        this.textList.clear();
        this.textList.addAll(textList);
        resetMinHeight();
        postInvalidate();
    }

    //重设最小高度
    private void resetMinHeight() {
        int lastMinHeight = minHeight;
        minHeight = minPadding;
        int totalTextWidthC = 0;
        for (int i = 0; i < textList.size(); i++) {
            float width = mPaint.measureText(textList.get(i));
            totalTextWidthC += (width + textPadding * 2 + textPace);
            if (totalTextWidthC + textPace > distanceW) {  //判断width 是否够
                totalTextWidthC = (int) (width + textPadding * 2 + textPace);
                minHeight += baseItemHeight;
            }
            if (i == textList.size() - 1)
                this.totalTextWidth = totalTextWidthC;
        }
        minHeight += textPadding * 2;
        if (minHeight != lastMinHeight) {
            requestLayout();
        }
    }

    /**
     * @param addStr
     */
    public void addData(String addStr) {
        if (!isShowEndAdd) {    //新增加的字符串是否显示在末尾
            clickIndex = this.textList.size();
            this.textList.add(addStr);
            //TODO  待补充requestLayout逻辑
        } else {
            int lastListSize = textList.size() - 1;
            clickIndex = lastListSize;
            String endStr = textList.get(lastListSize);
            textList.set(lastListSize, addStr);
            textList.add(endStr);
            float lastWidth = mPaint.measureText(endStr);
            float addWidth = mPaint.measureText(addStr);
            int lastMinHeight = minHeight;
            int totalTextWidthC = (int) (totalTextWidth + (addWidth - lastWidth));
            for (int i = 0; i < 2; i++) {
                if (i > 0)
                    totalTextWidthC += (lastWidth + textPadding * 2 + textPace);
                if (totalTextWidthC + textPace > distanceW) {  //判断width 是否够
                    totalTextWidthC = (int) ((i == 0 ? addWidth : lastWidth) + textPadding * 2 + textPace);
                    minHeight += baseItemHeight;
                }
                if (i == 1)
                    this.totalTextWidth = totalTextWidthC;
            }
            if (minHeight != lastMinHeight) {
                requestLayout();
            }
        }
        postInvalidate();
    }

    public void clearDeleteType() {
        clickIndex = -1;
        isLongClick = false;
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (paddingHasChanged) {
                    postInvalidate();
                    paddingHasChanged = false;
                }
                if (System.currentTimeMillis() - touchDownTime > 500) {
                    if (null != listener) {
                        clickIndex = -1;
                        isLongClick = !isLongClick;
                        postInvalidate();
                    }
                } else {
                    int i = 0;
                    float x = event.getX();
                    float y = event.getY();
                    for (int[] arr : pointList) {
                        if (x >= arr[0] && x <= arr[2] && y >= arr[1] && y <= arr[3]) {
                            clickIndex = i;
                            break;
                        }
                        i++;
                    }

                    if (null != listener && clickIndex >= 0) {
                        if (isLongClick)
                            listener.setOnClickLongItemListener(clickIndex);
                        else
                            listener.setOnClickItemListener(clickIndex);
                        postInvalidate();
                    }
                }
                break;

            case MotionEvent.ACTION_DOWN:
                paddingHasChanged = false;
                touchDownTime = System.currentTimeMillis();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (textList.size() <= 0)
            return;
        int startTextTop = minPadding;
        int startTextLeft = 0;
        int totalTextWidth = 0;
        pointList.clear();
        mPaint.setTextSize(textSize);
        for (int i = 0; i < textList.size(); i++) {
            mPaint.setTextAlign(Paint.Align.LEFT);
            float width = mPaint.measureText(textList.get(i));
            int lastWidth = totalTextWidth;
            totalTextWidth += (width + textPadding * 2 + textPace);
            if (totalTextWidth + textPace > distanceW) {  //判断width 是否够
                totalTextWidth = (int) (width + textPadding * 2 + textPace);
                startTextLeft = textPace;
                startTextTop += baseItemHeight;
            } else {
                startTextLeft = lastWidth + textPace;
            }

            mPaint.setColor(textNormalColor);
            boolean isShowClickType = i == clickIndex && null != listener && (!isShowEndAdd || i != textList.size() - 1) && !paddingHasChanged;
            mPaint.setStyle(isShowClickType ? Paint.Style.FILL : Paint.Style.STROKE);
            int[] array = {startTextLeft - textPadding, startTextTop - textPadding - textHeight,
                    (int) (startTextLeft + width + textPadding), startTextTop + textPadding};
            canvas.drawRect(array[0], array[1], array[2], array[3], mPaint);
            mPaint.setColor(isShowClickType ? textClickColor : textNormalColor);
            canvas.drawText(textList.get(i), startTextLeft, startTextTop, mPaint);
            if (isLongClick && null != listener && (!isShowEndAdd || i != textList.size() - 1) && !paddingHasChanged) {
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setColor(textClickColor);
                canvas.drawCircle(startTextLeft + width + textPadding, startTextTop - textPadding * 2, textPadding, mPaint);
                mPaint.setColor(textNormalColor);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(startTextLeft + width + textPadding, startTextTop - textPadding * 2, textPadding, mPaint);
                mPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("x", startTextLeft + width + textPadding, startTextTop - textPadding * 2 + textHeight / 2, mPaint);
                int part = textPadding + 5;
                array[1] -= part;
                array[2] += part;
                array[0] = array[2] - part * 2;
                array[3] = array[1] + part * 2;
            }
            pointList.add(array);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int resultWidth = sizeWidth;
        int resultHeight = sizeHeight;
        // 考虑内边距对尺寸的影响
        resultWidth += getPaddingLeft() + getPaddingRight();
        resultHeight += getPaddingTop() + getPaddingBottom();
        // 考虑父容器对尺寸的影响
        distanceW = resultWidth = resolveMeasure(sizeWidth, resultWidth);
        resultHeight = resolveMeasure(sizeHeight, resultHeight);
        Log.d(TAG, "onMeasure: minHeight: " + minHeight + ",resultHeight: " + resultHeight);
        setMeasuredDimension(resultWidth, minHeight);
    }

    /**
     * 根据传入的值进行测量
     */
    public int resolveMeasure(int measureSpec, int defaultSize) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                //设置warp_content时设置默认值
                result = Math.min(specSize, defaultSize);
                break;
            default:
                result = defaultSize;
        }
        return result;
    }

    public void removeItem(int index) {
        textList.remove(index);
        clickIndex = -1;
        resetMinHeight();
        postInvalidate();
    }

    public void setFlowClickListener(FlowClickListener clickListener) {
        this.listener = clickListener;
    }

    public interface FlowClickListener {
        void setOnClickItemListener(int index);

        void setOnClickLongItemListener(int index);
    }
}