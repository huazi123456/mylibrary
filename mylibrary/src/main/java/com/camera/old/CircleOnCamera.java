package com.camera.old;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * 焦点显示
 * Created by xyh on 2017/9/20
 */
public class CircleOnCamera extends View {
    private int mScreenWidth;
    private int mScreenHeight;
    private Paint mPaint;
    private Point centerPoint;
    private int radio;
    private boolean isShow = false;

    public CircleOnCamera(Context context) {
        this(context, null);
    }

    public CircleOnCamera(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleOnCamera(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getScreenMetrix(context);
        initView();
    }

    private void getScreenMetrix(Context context) {
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);// 空心
        centerPoint = new Point(mScreenWidth / 2, mScreenHeight / 2);
        radio = (int) (mScreenWidth * 0.06);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShow) {
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(centerPoint.x, centerPoint.y, radio, mPaint);// 画圆
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                centerPoint = new Point(x, y);
        }
        return false;
    }

    public void update() {
        isShow = true;
        invalidate();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                isShow = false;
                invalidate();
            }
        }, 500);
    }
}
