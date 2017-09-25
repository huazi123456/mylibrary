/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.permission;

import android.content.Context;

/**
 * Created by xyh on 2017/9/22
 */
public class MyPermission {

    private Context mContext;
    private PermissionListener mCallback;
    private int mRequestCode = 999999;//默认
    private String[] mPermission;

    //多个权限请求
    public MyPermission(Context context, String[] permissions, int requestCode, PermissionListener callback) {
        this.mContext = context;
        this.mPermission = permissions;
        this.mRequestCode = requestCode;
        this.mCallback = callback;
    }

    //多个权限请求
    public MyPermission(Context context, String[] permissions, PermissionListener callback) {
        this.mContext = context;
        this.mPermission = permissions;
        this.mCallback = callback;
    }

    //单个权限请求
    public MyPermission(Context context, String permissions, int requestCode, PermissionListener callback) {
        this.mContext = context;
        this.mPermission = new String[]{permissions};
        this.mRequestCode = requestCode;
        this.mCallback = callback;
    }

    //单个权限请求
    public MyPermission(Context context, String permissions, PermissionListener callback) {
        this.mContext = context;
        this.mPermission = new String[]{permissions};
        this.mCallback = callback;
    }

    public void request() {
        new PermissionManage(mContext)
                .requestCode(mRequestCode)
                .permission(mPermission)
                .callback(mCallback)
                .start();
    }

}
