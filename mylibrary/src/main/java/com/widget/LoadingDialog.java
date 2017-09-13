package com.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.mylibrary.R;


/**
 * 自定义加载框
 * Created by zhengjiabin on 2016/7/13.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lodingdiloag_xml, null);
        setContentView(view);
    }
}
