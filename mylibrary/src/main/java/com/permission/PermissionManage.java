package com.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 我的权限管理
 * Created by xyh on 2017/9/22.
 */

public class PermissionManage implements PermissionActivity.PermissionListener {

    private Context mContext;
    private int mRequestCode;
    private String[] mPermissions;
    private String[] mDeniedPermissions;
    private PermissionListener mCallback;

    public PermissionManage(Context context) {
        this.mContext = context;
    }

    @NonNull
    public PermissionManage requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @NonNull
    public PermissionManage permission(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public PermissionManage callback(PermissionListener callback) {
        this.mCallback = callback;
        return this;
    }

    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackSucceed();
        } else {
            mDeniedPermissions = getDeniedPermissions(mContext, mPermissions);
            if (mDeniedPermissions.length > 0) {
                PermissionActivity.setPermissionListener(this);
                Intent intent = new Intent(mContext, PermissionActivity.class);
                intent.putExtra(PermissionActivity.KEY_INPUT_PERMISSIONS, mDeniedPermissions);
                intent.putExtra(PermissionActivity.KEY_INPUT_REQUEST_CODE, mRequestCode);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                callbackSucceed();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String[] getDeniedPermissions(Context context, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permission);
        return deniedList.toArray(new String[deniedList.size()]);
    }

    private void callbackSucceed() {
        if (mCallback != null) {
            mCallback.onSucceed(mRequestCode, Arrays.asList(mPermissions));
        }
    }

    private void callbackFailed(List<String> deniedList) {
        if (mCallback != null) {
            mCallback.onFailed(mRequestCode, deniedList);
        }
    }

    @Override
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++)
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                deniedList.add(permissions[i]);

        if (deniedList.isEmpty())
            callbackSucceed();
        else
            callbackFailed(deniedList);
    }
}
