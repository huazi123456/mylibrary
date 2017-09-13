package com.irecyclerview;

import android.content.Context;
import android.util.AttributeSet;

import com.irecyclerview.xrecyclerview.ProgressStyle;
import com.irecyclerview.xrecyclerview.XRecyclerView;


/**
 * 自定义RecyclerView 继承XRecyclerView
 * 封装公共操作
 * Created by zhengjiabin on 2016/5/12.
 */
public class IRecyclerView extends XRecyclerView {

    private Context mContext;

    /**
     * 构造函数
     */
    public IRecyclerView(Context context) {
        super(context, null);
        mContext = context;
        iniView();
    }

    /**
     * 构造函数
     */
    public IRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
        iniView();
    }

    /**
     * 构造函数
     */
    public IRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        iniView();
    }

    /**
     * 初始化页面参数
     */
    private void iniView() {
        setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        //setArrowImageView(R.mipmap.iconfont_downgrey);
    }

    /**
     * 设置箭头图标
     */
    public void setCustomerArrowImageView(int resId) {
        setArrowImageView(resId);
    }
}
