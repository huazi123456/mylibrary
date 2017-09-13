package com.irecyclerview.xrecyclerview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicator.BallBeatIndicator;
import com.wang.avi.indicator.BallClipRotateIndicator;
import com.wang.avi.indicator.BallClipRotateMultipleIndicator;
import com.wang.avi.indicator.BallClipRotatePulseIndicator;
import com.wang.avi.indicator.BallGridBeatIndicator;
import com.wang.avi.indicator.BallGridPulseIndicator;
import com.wang.avi.indicator.BallPulseIndicator;
import com.wang.avi.indicator.BallPulseRiseIndicator;
import com.wang.avi.indicator.BallPulseSyncIndicator;
import com.wang.avi.indicator.BallRotateIndicator;
import com.wang.avi.indicator.BallScaleIndicator;
import com.wang.avi.indicator.BallScaleMultipleIndicator;
import com.wang.avi.indicator.BallScaleRippleIndicator;
import com.wang.avi.indicator.BallScaleRippleMultipleIndicator;
import com.wang.avi.indicator.BallSpinFadeLoaderIndicator;
import com.wang.avi.indicator.BallTrianglePathIndicator;
import com.wang.avi.indicator.BallZigZagDeflectIndicator;
import com.wang.avi.indicator.BallZigZagIndicator;
import com.wang.avi.indicator.BaseIndicatorController;
import com.wang.avi.indicator.CubeTransitionIndicator;
import com.wang.avi.indicator.LineScaleIndicator;
import com.wang.avi.indicator.LineScalePartyIndicator;
import com.wang.avi.indicator.LineScalePulseOutIndicator;
import com.wang.avi.indicator.LineScalePulseOutRapidIndicator;
import com.wang.avi.indicator.LineSpinFadeLoaderIndicator;
import com.wang.avi.indicator.PacmanIndicator;
import com.wang.avi.indicator.SemiCircleSpinIndicator;
import com.wang.avi.indicator.SquareSpinIndicator;
import com.wang.avi.indicator.TriangleSkewSpinIndicator;

/**
 * 重写AVLoadingIndicatorView 实现个性化需求
 * Created by zhengjiabin on 2016/7/18.
 */
public class XLoadingIndicatorView extends View {

    public XLoadingIndicatorView(Context context) {
        super(context);
        init();
    }

    //attrs
    int mIndicatorId = AVLoadingIndicatorView.BallPulse;
    int mIndicatorColor = Color.WHITE;

    Paint mPaint;

    BaseIndicatorController mIndicatorController;

    private boolean mHasAnimation;


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        applyIndicator();
    }

    public void setIndicatorId(int indicatorId) {
        mIndicatorId = indicatorId;
        applyIndicator();
    }

    public void setIndicatorColor(int color) {
        mIndicatorColor = color;
        mPaint.setColor(mIndicatorColor);
        this.invalidate();
    }

    private void applyIndicator() {
        switch (mIndicatorId) {
            case AVLoadingIndicatorView.BallPulse:
                mIndicatorController = new BallPulseIndicator();
                break;
            case AVLoadingIndicatorView.BallGridPulse:
                mIndicatorController = new BallGridPulseIndicator();
                break;
            case AVLoadingIndicatorView.BallClipRotate:
                mIndicatorController = new BallClipRotateIndicator();
                break;
            case AVLoadingIndicatorView.BallClipRotatePulse:
                mIndicatorController = new BallClipRotatePulseIndicator();
                break;
            case AVLoadingIndicatorView.SquareSpin:
                mIndicatorController = new SquareSpinIndicator();
                break;
            case AVLoadingIndicatorView.BallClipRotateMultiple:
                mIndicatorController = new BallClipRotateMultipleIndicator();
                break;
            case AVLoadingIndicatorView.BallPulseRise:
                mIndicatorController = new BallPulseRiseIndicator();
                break;
            case AVLoadingIndicatorView.BallRotate:
                mIndicatorController = new BallRotateIndicator();
                break;
            case AVLoadingIndicatorView.CubeTransition:
                mIndicatorController = new CubeTransitionIndicator();
                break;
            case AVLoadingIndicatorView.BallZigZag:
                mIndicatorController = new BallZigZagIndicator();
                break;
            case AVLoadingIndicatorView.BallZigZagDeflect:
                mIndicatorController = new BallZigZagDeflectIndicator();
                break;
            case AVLoadingIndicatorView.BallTrianglePath:
                mIndicatorController = new BallTrianglePathIndicator();
                break;
            case AVLoadingIndicatorView.BallScale:
                mIndicatorController = new BallScaleIndicator();
                break;
            case AVLoadingIndicatorView.LineScale:
                mIndicatorController = new LineScaleIndicator();
                break;
            case AVLoadingIndicatorView.LineScaleParty:
                mIndicatorController = new LineScalePartyIndicator();
                break;
            case AVLoadingIndicatorView.BallScaleMultiple:
                mIndicatorController = new BallScaleMultipleIndicator();
                break;
            case AVLoadingIndicatorView.BallPulseSync:
                mIndicatorController = new BallPulseSyncIndicator();
                break;
            case AVLoadingIndicatorView.BallBeat:
                mIndicatorController = new BallBeatIndicator();
                break;
            case AVLoadingIndicatorView.LineScalePulseOut:
                mIndicatorController = new LineScalePulseOutIndicator();
                break;
            case AVLoadingIndicatorView.LineScalePulseOutRapid:
                mIndicatorController = new LineScalePulseOutRapidIndicator();
                break;
            case AVLoadingIndicatorView.BallScaleRipple:
                mIndicatorController = new BallScaleRippleIndicator();
                break;
            case AVLoadingIndicatorView.BallScaleRippleMultiple:
                mIndicatorController = new BallScaleRippleMultipleIndicator();
                break;
            case AVLoadingIndicatorView.BallSpinFadeLoader:
                mIndicatorController = new BallSpinFadeLoaderIndicator();
                break;
            case AVLoadingIndicatorView.LineSpinFadeLoader:
                mIndicatorController = new LineSpinFadeLoaderIndicator();
                break;
            case AVLoadingIndicatorView.TriangleSkewSpin:
                mIndicatorController = new TriangleSkewSpinIndicator();
                break;
            case AVLoadingIndicatorView.Pacman:
                mIndicatorController = new PacmanIndicator();
                break;
            case AVLoadingIndicatorView.BallGridBeat:
                mIndicatorController = new BallGridBeatIndicator();
                break;
            case AVLoadingIndicatorView.SemiCircleSpin:
                mIndicatorController = new SemiCircleSpinIndicator();
                break;
        }
        mIndicatorController.setTarget(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(AVLoadingIndicatorView.DEFAULT_SIZE), widthMeasureSpec);
        int height = measureDimension(dp2px(AVLoadingIndicatorView.DEFAULT_SIZE), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END);
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mHasAnimation) {
            mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL);
    }

    void drawIndicator(Canvas canvas) {
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation() {
        mIndicatorController.initAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }

}
