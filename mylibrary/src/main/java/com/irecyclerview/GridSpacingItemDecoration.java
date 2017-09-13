package com.irecyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.utils.DimensUtil;

/**
 * Recycle Grid间距设置
 * Created by zhengjiabin on 2016/8/3.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private IRecyclerView mIRecyclerView;

    /**
     * @param spanCount   列数
     * @param spacing     间距 dp
     * @param includeEdge 是否启用边缘间距
     */
    public GridSpacingItemDecoration(Context context, IRecyclerView recyclerView, int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = DimensUtil.dpToPixels(context, spacing);
        this.includeEdge = includeEdge;
        this.mIRecyclerView = recyclerView;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = (position - mIRecyclerView.getHeaderCount() + 1) % spanCount; // item column
        if (position < mIRecyclerView.getHeaderCount() || mIRecyclerView.isEmptyView(position)) {
            return;
        }

        if (includeEdge) {
            //首行top为0
//            if ((position - mIRecyclerView.getHeaderCount()) < spanCount) {
//                outRect.top = 0;
//                outRect.left = spacing;
//            } else {
//                outRect.top = spacing; // item top
//                outRect.left = spacing;
//            }
            outRect.top = spacing; // item top
            outRect.left = spacing;
            //最后一列
            if (column == 0) {
                outRect.right = spacing;
            }
        } else {
            if (column != 0) {
                outRect.right = spacing;
            }
        }
    }
}