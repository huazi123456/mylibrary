package com.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylibrary.R;
import com.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;



/**
 * Activity 基类 继承AppCompatActivity
 * Created by zhengjiabin on 2016/7/11.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    protected String ACTIVITY_NAME = this.getClass().getSimpleName() + ":";
    private int mToolBarResId;
    private ToolBarHelper mToolBarHelper;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private TextView mRightTvOp;
    private ImageView mRightIvOp;
    protected LinearLayout mLlytBack;
    protected Context mContext;
    private boolean isRegisterEnentBus = false;

    protected void onCreate(Bundle savedInstanceState, int layoutResId, int toolBarResId) {
        mToolBarResId = toolBarResId;
        super.onCreate(savedInstanceState);
        setContentView(layoutResId);
        mContext = this;
        Log.i("ActivityName", "所在Activity = " + ACTIVITY_NAME);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mToolBarResId > 0) {
            mToolBarHelper = new ToolBarHelper(this, layoutResID, mToolBarResId);
            Toolbar toolbar = mToolBarHelper.getToolBar();
            setContentView(mToolBarHelper.getContentView());
            /**把 toolbar 设置到Activity 中*/
            setSupportActionBar(toolbar);
            /**自定义的一些操作*/
            toolbar.setContentInsetsRelative(0, 0);
            if (mToolBarResId == R.layout.title_toolbar) {
                mTvTitle = (TextView) toolbar.findViewById(R.id.mTvTitle);
                mIvBack = (ImageView) toolbar.findViewById(R.id.mIvBack);
                mRightTvOp = (TextView) toolbar.findViewById(R.id.mRightTvOp);
                mRightIvOp = (ImageView) toolbar.findViewById(R.id.mRightIvOp);
                mLlytBack = (LinearLayout) toolbar.findViewById(R.id.mLlytBack);
                if (mLlytBack != null) {
                    mLlytBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finishAnimation();
                        }
                    });
                }
            }
        } else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 获取后退布局
     */
    protected LinearLayout getBackLayout() {
        return mLlytBack;
    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
        isRegisterEnentBus = true;
    }

    /**
     * 设置标题
     */
    protected void setTitle(String title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    /**
     * 获取右侧文字操作控件
     */
    protected TextView getRightTvOp() {
        return mRightTvOp;
    }

    /**
     * 获取右侧图片操作控件
     */
    protected ImageView getRightIvOp() {
        return mRightIvOp;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRegisterEnentBus) {
            EventBus.getDefault().unregister(this);
            isRegisterEnentBus = false;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    private LoadingDialog mLoadingDialog;

    /**
     * 拥有默认动作的启动activity方法
     *
     * @param intent   要启动activity的意图
     * @param isFinish 是否关闭当前activity
     */
    public void startActivity(Intent intent, boolean isFinish) {
        super.startActivity(intent);
        if (isFinish) {
            finish();
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 拥有默认动作的启动activity方法
     *
     * @param intent   要启动activity的意图
     * @param reqCode  请求码
     * @param isFinish 是否关闭当前activity
     */
    public void startActivityForResult(Intent intent, int reqCode, boolean isFinish) {
        super.startActivityForResult(intent, reqCode);
        if (isFinish) {
            finish();
        }
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 使用默认动作关闭
     */
    public void finishAnimation() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
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

    /**
     * 显示对话框
     */
    protected void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder mDialog =
                new AlertDialog.Builder(this);
        mDialog.setMessage("你要点击哪一个按钮呢?");
        mDialog.setTitle("提示");
        mDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        mDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        mDialog.show();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
