package com.example.color.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.color.R;
import com.example.color.bean.colors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * @author Zoello
 * @description: 色盘选择器
 * @date : 2021/7/5 9:50
 */
public class Color2 extends View {

    private Paint circlePaint;
    private Paint touchPaint, colorPaint;
    private int lastX = -100;
    private int lastY = -100;
    private int radius;
    private int center;
    private int rgb = Color.TRANSPARENT;
    private List<com.example.color.bean.colors> colors;
    private Bitmap bitmap;
    private Paint newp;

    public Color2(Context context) {
        this(context, null);
        init();
    }

    public Color2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public Color2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        colors = new ArrayList<>();
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStyle(Paint.Style.FILL);

        newp = new Paint();
        newp.setAntiAlias(true); // 抗锯齿
        newp.setDither(true); // 防抖动
        newp.setStyle(Paint.Style.FILL);

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
        //阴影色
        circlePaint.setShadowLayer(10, 0, 0, Color.GRAY);
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        int colorCount = 12;
        int colorAngleStep = 360 / colorCount;
        int ncolors[] = new int[colorCount + 1];
        float hsv[] = new float[]{0f, 1f, 1f};
        for (int i = 0; i < ncolors.length; i++) {
            hsv[0] = 360 - (i * colorAngleStep) % 360;
            ncolors[i] = Color.HSVToColor(hsv);
        }
        ncolors[colorCount] = ncolors[0];
        SweepGradient sweepGradient = new SweepGradient(center, center, ncolors, null);
        RadialGradient radialGradient = new RadialGradient(center, center, radius, 0xFFFFFFFF, 0x00FFFFFF, Shader.TileMode.CLAMP);
        ComposeShader composeShader = new ComposeShader(sweepGradient, radialGradient, PorterDuff.Mode.SRC_OVER);
        circlePaint.setShader(composeShader);
        canvas.drawCircle(center, center, radius, circlePaint);
        //圆形渐变
        RadialGradient radialGradient1 = new RadialGradient(center, center, radius-120, Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP);
        newp.setShader(radialGradient1);
        canvas.drawCircle(center,center,radius-120,newp);


        Canvas canvas1 = new Canvas(bitmap);
        canvas1.drawCircle(center, center, radius, circlePaint);
        canvas1.drawCircle(center, center,radius-120,newp);

        //储存bitmap全部的色值
        if (colors.size() == 0) {
            for (int i = 0; i < bitmap.getHeight(); i++) {
                for (int j = 0; j < bitmap.getWidth(); j++) {
                    int color = bitmap.getPixel(j, i);
                    colors.add(new colors(color, new int[]{j, i}));
                }
            }
        }
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
        if (distanceZ > radius - 40) {
            double ratio = (radius - 40) /
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
        if (bitmap != null) {
            int color = bitmap.getPixel(lastX, lastY);
            int alpha = color >>> 24;
            int red = (color & 0xff0000) >> 16;
            int green = (color & 0x00ff00) >> 8;
            int blue = (color & 0x0000ff);
            rgb = Color.rgb(red, green, blue);
            if (ColorSeleListener != null) {
                ColorSeleListener.onColorSele(alpha, red, green, blue);
            }
        }
        postInvalidate();
        // 自己处理触摸事件
        return true;
    }

    public interface onColorSeleListener {
        void onColorSele(int alpha, int red, int green, int blue);
    }

    private onColorSeleListener ColorSeleListener;

    public void setColorSeleListener(onColorSeleListener onColorSeleListener) {
        this.ColorSeleListener = onColorSeleListener;
    }

    public boolean findBitmapColor(int oldColor) {
        if (colors.size() != 0) {
            for (int i = 0; i < colors.size(); i++) {
                if (colors.get(i).getColor() == oldColor) {
                    lastX = colors.get(i).getXy()[0];
                    lastY = colors.get(i).getXy()[1];
                    rgb = oldColor;
                    postInvalidate();
                    return true;
                }
            }
        } else {
            return false;
        }
        return  false;
    }
}

