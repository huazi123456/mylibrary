package com.base;

/**
 * Created by admin on 2017/9/1.
 */

import android.os.Bundle;
import android.view.View;

import com.irecyclerview.RecycleBaseAdapter;
import com.irecyclerview.xrecyclerview.XRecyclerView;

import java.util.List;
/**
        * 实现RecyclerView的Activity基类
        * Created by Hello on 2016/5/12.
        */
public abstract class BaseRecyclerActivity<T extends XRecyclerView> extends BaseAppCompatActivity {

    protected T mRecyclerView;
    protected RecycleBaseAdapter mAdapter;
    private int totalCount;
    protected int pageIndex = 1;
    protected int pageSize = 20;
    protected View mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState, int layoutResId, int layoutActionBar) {
        super.onCreate(savedInstanceState, layoutResId, layoutActionBar);
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

    protected abstract void getData();

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

}
