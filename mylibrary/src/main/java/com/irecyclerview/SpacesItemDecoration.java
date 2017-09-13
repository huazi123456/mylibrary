package com.irecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.utils.DimensUtil;


/**
 * RecycleView 自定义间距
 * Created by zhengjiabin on 2016/7/21.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    /**
     * 上下左右都间隔
     */
    public static final int FULLSPACE = 0;
    /**
     * 左侧间隔
     */
    public static final int LEFTSPACE = 1;
    /**
     * 右侧间隔
     */
    public static final int RIGHTSPACE = 2;
    /**
     * 顶部间隔
     */
    public static final int TOPSPACE = 3;
    /**
     * 底部间隔
     */
    public static final int BOTTOMSPACE = 4;

    private int mSpaceType = 0;

    private IRecyclerView mIRecyclerView;

    /***
     * RecycleView自定义间距
     *
     * @param space        间距 单位为dp
     * @param recyclerView IRecyclerView控件
     * @param spaceType    间隔类型 SpacesItemDecoration.FULLSPACE 等多种类型可使用
     */
    public SpacesItemDecoration(Context context, IRecyclerView recyclerView, int space, int spaceType) {
        this.space = DimensUtil.dpToPixels(context, space);
        this.mSpaceType = spaceType;
        this.mIRecyclerView = recyclerView;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (mSpaceType) {
            case FULLSPACE:
                outRect.top = space;
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                break;
            case LEFTSPACE:
                outRect.left = space;
                break;
            case RIGHTSPACE:
                outRect.right = space;
                break;
            case TOPSPACE:
                outRect.top = space;
                if (mIRecyclerView != null) {
                    //首条数据和头部下拉刷新控件Top为0
                    if (parent.getChildLayoutPosition(view) <= mIRecyclerView.getHeaderCount()) {
                        outRect.top = 0;
                    }
                }
                break;
            case BOTTOMSPACE:
                outRect.bottom = space;
                if (mIRecyclerView != null) {
                    //头部下拉刷新控件、最后一条数据和底部加载更多控件Bottom为0
                    if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) >= mIRecyclerView.getAdapter().getItemCount() - mIRecyclerView.getFooterCount() - 1) {
                        outRect.bottom = 0;
                    }
                }
                break;
        }
    }
}
