package com.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylibrary.R;
import com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;



/**
 * fragment的基类
 * Created by xyh on 2016/7/21.
 */

public abstract class BaseFragment extends Fragment {

    private boolean isRegisterEnentBus = false;
    protected View mView;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    protected View onCreateView(LayoutInflater inflater, ViewGroup container, int layoutId) {
        mView = inflater.inflate(layoutId, container, false);
        return mView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        if (isRegisterEnentBus) {
            EventBus.getDefault().unregister(this);
            isRegisterEnentBus = false;
        }
    }

    public <T extends View> T findView(int id) {
        if (mView != null) {
            return (T) mView.findViewById(id);
        } else {
            return null;
        }

    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
        isRegisterEnentBus = true;
    }

    private LoadingDialog mLoadingDialog;

    /**
     * 拥有默认动作的启动activity方法
     *
     * @param intent   要启动activity的意图
     * @param isFinish 是否关闭当前activity
     */
    protected void startActivity(Intent intent, boolean isFinish) {
        getActivity().startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 拥有默认动作的启动activity方法
     *
     * @param intent   要启动activity的意图
     * @param reqCode  请求码
     * @param isFinish 是否关闭当前activity
     */
    protected void startActivityForResult(Intent intent, int reqCode, boolean isFinish) {
        getActivity().startActivityForResult(intent, reqCode);
        if (isFinish) {
            getActivity().finish();
        }
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 使用默认动作关闭
     */
    protected void finishAnimation() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载框
     */
    public void hideLoding() {
        if (mLoadingDialog != null) {
            mLoadingDialog.hide();
        }
    }

}
