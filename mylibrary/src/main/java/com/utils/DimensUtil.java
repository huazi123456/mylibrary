package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 像素转换工作类
 * Created by zhengjiabin on 2016/7/14.
 */
public class DimensUtil {
    public DimensUtil() {
    }

    public static int dpToPixels(Context context, float dp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(1, dp, res.getDisplayMetrics());
        return (int) px;
    }

    public static int pixelsToDp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5F);
    }

    public static int pixelsToSp(Context context, float px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5F);
    }

    public static int spToPixels(Context context, float sp) {
        Resources res = context.getResources();
        float px = TypedValue.applyDimension(2, sp, res.getDisplayMetrics());
        return (int) px;
    }

    public static int getDisplayWidth(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getDisplayHeight(Context context) {
        return ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
    }
}