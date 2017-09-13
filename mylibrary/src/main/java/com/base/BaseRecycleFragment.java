package com.base;

import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irecyclerview.RecycleBaseAdapter;
import com.irecyclerview.xrecyclerview.XRecyclerView;
import com.utils.ScreenUtil;

import java.util.List;


/**
 * 实现RecyclerView的Fragment基类
 * Created by zhengjiabin on 2016/7/21.
 */
public abstract class BaseRecycleFragment<T extends XRecyclerView> extends BaseFragment {
    protected T mRecyclerView;
    protected RecycleBaseAdapter mAdapter;
    private int totalCount;
    protected int pageIndex = 1;
    protected int pageSize = 20;
    protected View mEmptyView;
    protected View mTopBarView;
    private SparseArray<View> views = new SparseArray<View>();
    protected boolean mIsMarginTop = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, int layoutId) {
        mView = super.onCreateView(inflater, container, layoutId);
        return mView;
    }

    /**
     * 初始化页面 基类中处理相同逻辑
     */
    protected void initView() {
        setRecyclerView();
        initHeader();
        setListener();
        setLayoutManager();
        initAdapter();
        setEmptyView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsMarginTop) {
            changeTopBarMargin(getTopBarLayoutParams(), ScreenUtil.getStatusBarHeight(getContext()));
        }
    }


    private void changeTopBarMargin(ViewGroup.MarginLayoutParams params, int statusBarHeight) {
        if (params == null) return;
        params.topMargin = statusBarHeight;
    }


    /**
     * 获取子Fragment的顶部导航栏目的layoutParams
     */
    private ViewGroup.MarginLayoutParams getTopBarLayoutParams() {
        mTopBarView = getTopBarView(mView);
        if (mTopBarView == null) {
            return null;
        }
        ViewGroup.LayoutParams layoutParams = mTopBarView.getLayoutParams();
        return (layoutParams instanceof ViewGroup.MarginLayoutParams) ? (ViewGroup.MarginLayoutParams) layoutParams : null;
    }

    /**
     * 需要适配沉浸式的子Fragment需要重写此方法 返回子Fragmennt的顶部导航栏
     *
     * @param rootView
     * @return
     */
    protected View getTopBarView(View rootView) {
        return null;
    }

    /**
     * 设置RecyclerView
     */
    protected abstract void setRecyclerView();

    /**
     * 初始化数据适配器
     */
    protected abstract void initAdapter();

    /**
     * 初始化头部控件
     */
    protected abstract void initHeader();

    /**
     * 设置RecycleView显示风格
     */
    protected abstract void setLayoutManager();

    /**
     * 初始化空数据时显示View
     */
    protected abstract void setEmptyView();


    /**
     * 获取数据
     */
    protected abstract void getData();

    /**
     * 注册事件
     */
    private void setListener() {
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getData();
            }

            @Override
            public void onLoadMore() {
                ++pageIndex;
                getData();
            }
        });
    }

    /**
     * 执行结果
     */
    protected void executeOnLoadDataSuccess(List<?> data, int totalCount) {
        this.totalCount = totalCount;
        if (pageIndex == 1 && this.mAdapter != null) {
            this.mAdapter.clear();
        }
        if (this.mAdapter != null && data != null && data.size() > 0) {
            this.mAdapter.addDatas(data);
        }
        if (mRecyclerView != null) {
            if (pageIndex == 1) {
                if (totalCount == 0) {
                    mRecyclerView.showEmptyView();
                } else {
                    mRecyclerView.hideEmptyView();
                }
                mRecyclerView.refreshComplete();
            } else {
                mRecyclerView.loadMoreComplete();
            }
            if (totalCount > 0) {
                if (pageIndex * pageSize < totalCount) {
                    mRecyclerView.setNoMore(false);
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        }
    }

    protected <T extends View> T getView(int id) {
        T view = (T) views.get(id);
        if (null == view) {
            view = (T) mView.findViewById(id);
            views.put(id, view);
        }
        return view;
    }
}
