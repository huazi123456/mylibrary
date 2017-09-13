
package com.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Window;

import java.lang.reflect.Field;

/**
 * <p>类描述：获取设备屏幕长宽和密度比的工具类
 * 使用前需先初始化工具类 --->getInstance().init(mContext) ;
 * <p>作   者：will
 */
public class ScreenUtil {
    private static ScreenUtil sInstance;

    private int mScreenWidth;

    private int mScreenHeight;

    private float mDensity;

    public static ScreenUtil getInstance() {
        if (sInstance == null) {
            sInstance = new ScreenUtil();
        }
        return sInstance;
    }

    /**
     * 初始化工作
     *
     * @param context
     */
    public void init(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mDensity = dm.density;
    }

    /**
     * 调用之前请确认是否调用 {@link #init(Context)}进行初始化
     *
     * @return 屏幕的宽度
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * 调用之前请确认是否调用 {@link #init(Context)}进行初始化
     *
     * @return 屏幕的高度
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    public float getDensity() {
        return mDensity;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取状态栏高度＋标题栏(ActionBar)高度
     */
    public int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
    }

    /**
     * 获取通知栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {

        }
        return statusBarHeight;
    }
}
