package com.example.color.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.color.R;

import androidx.annotation.Nullable;

/**
 * @author Zoello
 * @description: 线性多色选择器
 * @date : 2021/7/5 9:50
 */
public class Color1 extends View {

    private Paint circlePaint;
    private final int startcolor;
    private final int middlecolor;
    private final int endcolor;
    private Paint touchPaint, colorPaint;
    private int lastX = -100;
    private int lastY = -100;
    private int radius;
    private int center;
    private Bitmap target;
    private int rgb = Color.TRANSPARENT;

    public Color1(Context context) {
        this(context, null);
    }

    public Color1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Color1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.color1);
        //底色亮灰色
        startcolor = typedArray.getColor(R.styleable.color1_start_color, Color.parseColor("#A1BBFF"));
        middlecolor = typedArray.getColor(R.styleable.color1_middle_color, Color.parseColor("#FFFFFF"));
        endcolor = typedArray.getColor(R.styleable.color1_end_color, Color.parseColor("#F6A151"));
        typedArray.recycle();
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStyle(Paint.Style.FILL);

        touchPaint = new Paint();
        touchPaint.setAntiAlias(true);
        touchPaint.setDither(true);
        touchPaint.setStyle(Paint.Style.STROKE);
        touchPaint.setStrokeWidth(5);
        touchPaint.setColor(Color.parseColor("#FFFFFF"));
        touchPaint.setShadowLayer(10, 0, 0, Color.GRAY);
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setDither(true);
        colorPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 测量尺寸时的回调方法
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //中心点
        center = getWidth() / 2;
        //半径
        radius = getWidth() / 2;
        //画圆
        drawCircle(canvas, center, radius - 10);

        //画手指触摸
        drawTouch(canvas, 40);

    }

    private void drawCircle(Canvas canvas, int center, int radius) {
        int[] colorArray = new int[]{startcolor, middlecolor, endcolor};
        float[] positionArray = new float[]{0f, 0.5f, 1f};
        circlePaint.setShader(new LinearGradient(0, 0, 0, getWidth(), colorArray, positionArray, Shader.TileMode.CLAMP));
        //阴影色
        circlePaint.setShadowLayer(10, 0, 0, Color.GRAY);
        //  绘制实心圆
        canvas.drawCircle(center, center, radius, circlePaint);


        target = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(target);
        canvas1.drawCircle(center, center, radius, circlePaint);
    }

    private void drawTouch(Canvas canvas, int radius) {
        canvas.drawCircle(lastX, lastY, radius, touchPaint);
        colorPaint.setColor(rgb);
        canvas.drawCircle(lastX, lastY, radius - 3, colorPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //点击位置x坐标与圆心的x坐标的距离
        int distanceX = Math.abs(center - x);
        //点击位置y坐标与圆心的y坐标的距离
        int distanceY = Math.abs(center - y);
        //点击位置与圆心的直线距离
        int distanceZ = (int) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        //如果点击位置与圆心的距离大于圆的半径，证明点击位置没有在圆内
        if (distanceZ > radius - 50) {
            double ratio = (radius - 50) /
                    Math.sqrt(
                            Math.pow(x - center, 2) +
                                    Math.pow(y - center, 2));
            float cx2 = (float) (ratio * (x - center) + center);
            float cy2 = (float) (ratio * (y - center) + center);
            lastX = (int) cx2;
            lastY = (int) cy2;
        } else {
            lastX = x;
            lastY = y;
        }
        setCallback();

        postInvalidate();
        // 自己处理触摸事件
        return true;
    }

    private void setCallback() {
        int i = lastY - 50;
        int j = getHeight() - 100;
        double v = (double) i / (double) j;
        int progress = (int) (v * 100);
        if (target != null) {
            int color = target.getPixel(lastX, lastY);
            int alpha = color >>> 24;
            int red = (color & 0xff0000) >> 16;
            int green = (color & 0x00ff00) >> 8;
            int blue = (color & 0x0000ff);
            rgb = Color.rgb(red, green, blue);
            if (ColorSeleListener != null) {
                ColorSeleListener.onColorSele(alpha, red, green, blue, progress);
            }
        }
    }

    public void setColorProgress(int progress) {
        lastX = center;
        //10 是内边距 40是触摸小球的高度
        if (progress > 50) {
            lastY = center + ((center - 10 - 20) / 50) * (progress - 50);
        } else {
            lastY = (((center) / 50) * progress) + 50;
        }
        setCallback();
        postInvalidate();
    }

    public interface onColorSeleListener {
        void onColorSele(int alpha, int red, int green, int blue, int progress);
    }

    private onColorSeleListener ColorSeleListener;

    public void setColorSeleListener(onColorSeleListener onColorSeleListener) {
        this.ColorSeleListener = onColorSeleListener;
    }

}

