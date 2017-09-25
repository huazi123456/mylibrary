package com.utils;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * Created by admin on 2017/9/19.
 */

public class OrientationUtils extends OrientationEventListener {
    public OrientationUtils(Context context) {
        super(context);
    }

    public OrientationUtils(Context context, int rate) {
        super(context, rate);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        }

        //保证只返回四个方向
        int newOrientation = ((orientation + 45) / 90 * 90) % 360;
        //Log.i("huazi","方位角度："+ newOrientation);
    }
}
