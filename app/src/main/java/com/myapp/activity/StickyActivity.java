package com.myapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.base.BaseRecyclerActivity;
import com.irecyclerview.BaseViewHolder;
import com.irecyclerview.IRecyclerView;
import com.irecyclerview.stickyheadersrecyclerview.StickyRecyclerAdapter;
import com.irecyclerview.stickyheadersrecyclerview.util.StickyRecyclerHeadersDecoration;
import com.irecyclerview.stickyheadersrecyclerview.util.StickyRecyclerHeadersTouchListener;
import com.myapp.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/9/15.
 */

public class StickyActivity extends BaseRecyclerActivity<IRecyclerView> {

    @BindView(R.id.mIRecyclerView)
    IRecyclerView mIRecyclerView;

    private StickyRecyclerAdapter mSrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, R.layout.layout_irecyclerview, R.layout.title_toolbar);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    @Override
    protected void initView() {
        setTitle("列表头顶页");
        super.initView();
    }

    private String[] getDummyDataSet() {
        return getResources().getStringArray(R.array.animals);
    }

    @Override
    protected void setRecyclerView() {
        mIRecyclerView.setPullRefreshEnabled(false);
        mIRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView = mIRecyclerView;
    }

    @Override
    protected void initAdapter() {
        mSrAdapter = new StickyRecyclerAdapter<String>(this, R.layout.item_text) {
            @Override
            public void convertHViewHolder(BaseViewHolder holder, String s) {
                holder.setText(R.id.mTvList, s);
                holder.setOnClickListener(R.id.mTvList, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

        };
        mAdapter = mSrAdapter;
        mIRecyclerView.setAdapter(mSrAdapter);

        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mSrAdapter);
        mIRecyclerView.addItemDecoration(headersDecor);

        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(mIRecyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(StickyActivity.this, "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        mIRecyclerView.addOnItemTouchListener(touchListener);
    }

    @Override
    protected void initHeader() {

    }

    @Override
    protected void setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mIRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void setEmptyView() {

    }

    @Override
    protected void getData() {
        List<String> list = Arrays.asList(getDummyDataSet());
        executeOnLoadDataSuccess(list, list.size());
    }
}
