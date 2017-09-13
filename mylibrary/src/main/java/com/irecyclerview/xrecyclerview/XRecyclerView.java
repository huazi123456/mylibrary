package com.irecyclerview.xrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mylibrary.R;

import java.util.ArrayList;
import java.util.List;

public class XRecyclerView extends RecyclerView {
    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_UP = 1;
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private int mRefreshProgressStyle = ProgressStyle.SysProgress;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFootViews = new ArrayList<>();
    private Adapter mWrapAdapter;
    private float mLastY = -1;
    private static final float DRAG_RATE = 3;
    private LoadingListener mLoadingListener;
    private OnScrolledListener mOnScrolledListener;
    private ArrowRefreshHeader mRefreshHeader;
    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;
    private static final int TYPE_NOFOOTER = -9;
    private static final int TYPE_EMPTYVIEW = -7;
    private static final int TYPE_REFRESH_HEADER = -5;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = -3;
    private static final int HEADER_INIT_INDEX = 10000;
    private static List<Integer> sHeaderTypes = new ArrayList<>();
    private int mPageCount = 0;
    //adapter没有数据的时候显示,类似于listView的emptyView
    private View mEmptyView;
    private final AdapterDataObserver mDataObserver = new DataObserver();
    //    private OnRefreshListener mOnRefreshListener;
    private static final int PULLFULL = 0;
    private static final int PullREFRESH = 1;
    private static final int PULLLOADMORE = 2;
    private int mPullType;
    private boolean isFreshing;
    private boolean isHomeSpecial = false;
    private int mCurrentDirection = -1;
    private OnScrollIdleListenter mOnScrollIdleListener;


    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setIsHomeSpecial(boolean isSpecial) {
        this.isHomeSpecial = isSpecial;
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.XRecyclerView);
        mPullType = array.getInt(R.styleable.XRecyclerView_pulltype, PULLFULL);
        switch (mPullType) {
            case PULLFULL:
                pullRefreshEnabled = true;
                loadingMoreEnabled = true;
                break;
            case PullREFRESH:
                pullRefreshEnabled = true;
                loadingMoreEnabled = false;
                break;
            case PULLLOADMORE:
                pullRefreshEnabled = false;
                loadingMoreEnabled = true;
                break;
        }
        addArrowRefreshHeader();
        addLoadMoreFooter();
    }

    /**
     * 添加刷新头部
     */
    private void addArrowRefreshHeader() {
        if (pullRefreshEnabled) {
            ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(getContext());
            mHeaderViews.add(0, refreshHeader);
            mRefreshHeader = refreshHeader;
            mRefreshHeader.setProgressStyle(mRefreshProgressStyle);
        }
    }

    /**
     * 添加加载更多底部
     */
    private void addLoadMoreFooter() {
        if (loadingMoreEnabled) {
            LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
            footView.setProgressStyle(mLoadingMoreProgressStyle);
            addFootView(footView);
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        if (pullRefreshEnabled && !(mHeaderViews.get(0) instanceof ArrowRefreshHeader)) {
            addArrowRefreshHeader();
        }
        mHeaderViews.add(view);
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
    }

    public void addFootView(final View view) {
        mFootViews.clear();
        mFootViews.add(view);
    }

    /**
     * 加载更多完成
     */
    public void loadMoreComplete() {
        if (loadingMoreEnabled) {
            isLoadingData = false;
            View footView = mFootViews.get(0);
            if (footView instanceof LoadingMoreFooter) {
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_COMPLETE);
            } else {
                footView.setVisibility(View.GONE);
            }
        }
    }

    public void setNoMore(boolean noMore) {
        if (loadingMoreEnabled) {
            this.isNoMore = noMore;
            View footView = mFootViews.get(0);
            ((LoadingMoreFooter) footView).setState(this.isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
        }
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void noMoreLoading() {
        isLoadingData = false;
        View footView = mFootViews.get(0);
        isNoMore = true;
        if (footView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_NOMORE);
        } else {
            footView.setVisibility(View.GONE);
        }
    }

    /**
     * 完成刷新更多
     */
    public void refreshComplete() {
        if (pullRefreshEnabled) {
            mRefreshHeader.refreshComplete();
        }
    }

    public void setRefreshHeader(ArrowRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        if (mPullType == PULLFULL || mPullType == PULLLOADMORE) {
            loadingMoreEnabled = enabled;
            if (!enabled && mFootViews.size() > 0) {
                mFootViews.get(0).setVisibility(GONE);
            }
        }
    }

    public void setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
    }

    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootViews.size() > 0 && mFootViews.get(0) instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootViews.get(0)).setProgressStyle(style);
        }
    }

    public void setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
    }

    /**
     * 设置空数据显示View
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mEmptyView.setVisibility(View.GONE);
    }

    private boolean mIsShowEmptyView = false;

    /**
     * 显示空数据显示View
     */
    public void showEmptyView() {
        if (this.mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            setLoadingMoreEnabled(false);
            mIsShowEmptyView = true;
            if (mWrapAdapter != null)
                mWrapAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 隐藏空数据显示View
     */
    public void hideEmptyView() {
        if (mIsShowEmptyView) {
            mEmptyView.setVisibility(View.GONE);
            setLoadingMoreEnabled(true);
            mIsShowEmptyView = false;
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据
     */
    public void onRefresh() {
        if (mLoadingListener != null) {
            mLoadingListener.onRefresh();
        }
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        mWrapAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    public void resetAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver(mDataObserver);
        }
        setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (isHomeSpecial) {
            if (state == RecyclerView.SCROLL_STATE_IDLE && mOnScrollIdleListener != null) {
                mOnScrollIdleListener.onScrollIdle();
            }
            return;
        }
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                    && layoutManager.getItemCount() > layoutManager.getChildCount()
                    && !isNoMore) {
                if ((mRefreshHeader != null && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) || !pullRefreshEnabled) {
                    View footView = mFootViews.get(0);
                    isLoadingData = true;
                    if (footView instanceof LoadingMoreFooter) {
                        ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                    } else {
                        footView.setVisibility(View.VISIBLE);
                    }
                    mLoadingListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                if (deltaY > DRAG_RATE) mCurrentDirection = DIRECTION_DOWN;
                if (deltaY < -DRAG_RATE) mCurrentDirection = DIRECTION_UP;
                mLastY = ev.getRawY();
                if (isOnTop() && pullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE);
                    int state = mRefreshHeader.getState();
                    if (mRefreshHeader.getVisibleHeight() > 0 && state < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (isOnTop() && pullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public boolean isOnTop() {
        if (mRefreshHeader != null) {
            if (mRefreshHeader.getParent() != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
//        return !(mHeaderViews == null || mHeaderViews.isEmpty()) && mHeaderViews.get(0).getParent() != null;

//        LayoutManager layoutManager = getLayoutManager();
//        int firstVisibleItemPosition;
//        if (layoutManager instanceof GridLayoutManager) {
//            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        } else if ( layoutManager instanceof StaggeredGridLayoutManager ) {
//            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
//            ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(into);
//            firstVisibleItemPosition = findMin(into);
//        } else {
//            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        if ( firstVisibleItemPosition <= 1 ) {
//             return true;
//        }
//        return false;
    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    /**
     * 获取头部数量 包含下拉刷新
     */
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * 空视图
     */
    public boolean isEmptyView(int position) {
        return mWrapAdapter.getItemViewType(position) == TYPE_EMPTYVIEW;
    }


    /**
     * 获取底部数量 包含下拉加载更多
     */
    public int getFooterCount() {
        return mFootViews.size();
    }

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        private int headerPosition = 1;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isEmptyView(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()) || isEmptyView(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        public boolean isContentHeader(int position) {
            if (!pullRefreshEnabled) {
                return position >= 0 && position < mHeaderViews.size();
            }
            return position >= 1 && position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        /**
         * EmptyView放在Header集合下面
         */
        public boolean isEmptyView(int position) {
            return position == mHeaderViews.size() && adapter.getItemCount() == 0;
        }

        public boolean isRefreshHeader(int position) {
            return position == 0 && pullRefreshEnabled;
        }

        public int getHeadersCount() {
            return mHeaderViews.size() + getEmptyViewCount();
        }

        public int getEmptyViewCount() {
            return adapter.getItemCount() == 0 ? 1 : 0;
        }

        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                mCurrentPosition++;
                return new SimpleViewHolder(mHeaderViews.get(0));
            } else if (isContentHeader(mCurrentPosition)) {
                int headeIndex = mCurrentPosition;
                if (pullRefreshEnabled) {//开启下拉刷新组件时 头部类型索引应该减去下拉刷新组件的索引
                    headeIndex = headeIndex - 1;
                }
                if (viewType == sHeaderTypes.get(headeIndex)) {
                    mCurrentPosition++;
                    if (!pullRefreshEnabled) {//没有下拉刷新组件时 头部控件索引从0开始
                        headerPosition = headerPosition - 1;
                    }
                    return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
                }
            } else if (viewType == TYPE_EMPTYVIEW && isEmptyView(mCurrentPosition)) {
                mCurrentPosition++;
                if (mEmptyView != null) {
                    return new SimpleViewHolder(mEmptyView);
                } else {
                    return new SimpleViewHolder(new View(getContext()));
                }
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootViews.get(0));
            } else if (viewType == TYPE_NOFOOTER) {
                return new SimpleViewHolder(new View(getContext()));
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        private int mCurrentPosition;

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            if (isEmptyView(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                if (pullRefreshEnabled) {
                    position = position - 1;
                }
                return sHeaderTypes.get(position);
            }
            if (isEmptyView(position)) {
                return TYPE_EMPTYVIEW;
            }
            if (isFooter(position)) {
                if (!isHomeSpecial) {
                    if (adapter.getItemCount() == 0 || !loadingMoreEnabled) {
                        return TYPE_NOFOOTER;
                    } else {
                        return TYPE_FOOTER;
                    }
                } else {
                    if (((ViewGroup) mHeaderViews.get(1)).getChildCount() > 0 && loadingMoreEnabled) {
                        return TYPE_FOOTER;
                    } else {
                        return TYPE_NOFOOTER;
                    }
                }
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (mOnScrolledListener != null) {
            mOnScrolledListener.onScrolled(this, dx, dy);
        }
    }

    public void setmOnScrolledListener(OnScrolledListener listener) {
        this.mOnScrolledListener = listener;
    }

    public interface OnScrolledListener {
        void onScrolled(View view, int dx, int dy);
    }

    public void setOnScrollIdleListener(OnScrollIdleListenter onScrollStateChangeListener) {
        this.mOnScrollIdleListener = onScrollStateChangeListener;
    }

    public interface OnScrollIdleListenter {
        void onScrollIdle();
    }


    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {

        void onRefresh();

        void onLoadMore();
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            mLoadingListener.onRefresh();
        }
    }

    public boolean isLoadingData() {
        return isLoadingData;
    }

    public void setLoadingData(boolean loadingData) {
        isLoadingData = loadingData;
    }

    public boolean isNoMore() {
        return isNoMore;
    }

    public ArrayList<View> getmFootViews() {
        return mFootViews;
    }

    public ArrayList<View> getmHeaderViews() {
        return mHeaderViews;
    }

    public boolean isFreshing() {
        return isFreshing;
    }

    public void setFreshing(boolean freshing) {
        isFreshing = freshing;
    }

    public ArrowRefreshHeader getmRefreshHeader() {
        return mRefreshHeader;
    }

    public boolean isLoadMoreEnable() {
        return loadingMoreEnabled;
    }

    public int getmCurrentDirection() {
        return mCurrentDirection;
    }
}