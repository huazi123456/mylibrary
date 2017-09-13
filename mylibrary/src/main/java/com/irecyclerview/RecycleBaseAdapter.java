package com.irecyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 通用适配器
 * Created by zhengjiabin on 2016/7/13.
 */

public abstract class RecycleBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemSlideHelper.Callback {

    protected Context mContext;
    protected int mItemLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private IRecyclerView mRecyclerView;
    private int mPosition = 0;
    private SlideCallBack mCallBackOne;
    private SlideCallBack mCallBackTwo;
    private int mHeadItemCount;

    /**
     * RecyclerView 通用适配器构造函数
     *
     * @param context
     */
    public RecycleBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<T>();
    }

    /**
     * RecyclerView 通用适配器构造函数
     *
     * @param context
     * @param itemLayoutId 每一个Item需展示视图LayoutId
     */
    public RecycleBaseAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
        mDatas = new ArrayList<T>();
    }

    /**
     * 专为侧滑RecyclerView设计，注意使用时
     * 对RecyclerView的item的layout布局有要求
     *
     * @param context
     * @param itemLayoutId
     * @param iRecyclerView 当前RecyclerView
     */
    public RecycleBaseAdapter(Context context, int itemLayoutId, IRecyclerView iRecyclerView) {
        mRecyclerView = iRecyclerView;
        mHeadItemCount = mRecyclerView.getHeaderCount();
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this, mHeadItemCount, this));
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
        mDatas = new ArrayList<T>();
    }

    /**
     * RecyclerView 通用适配器构造函数
     *
     * @param context
     * @param itemLayoutId 每一个Item需展示视图LayoutId
     * @param datas        数据集合
     */
    public RecycleBaseAdapter(Context context, int itemLayoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemLayoutId = itemLayoutId;
        mDatas = datas;
    }

    /**
     * 设置侧滑回调事件，这边只做了侧滑时，最多2个item的处理
     *
     * @param callBackOne 第一个item的回调事件
     * @param callBackTwo 第二个item的回调事件
     */
    public void setSlideCallBack(SlideCallBack callBackOne, SlideCallBack callBackTwo) {
        mCallBackOne = callBackOne;
        mCallBackTwo = callBackTwo;
    }

    /**
     * 设置数据集合
     */
    public void setData(List<T> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    /**
     * 添加数据集合
     */
    public void addDatas(List<T> datas) {
        if (this.mDatas != null) {
            this.mDatas.addAll(datas);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 获取当前item位置
     */
    public int getPosition() {
        return mPosition;
    }

    /**
     * 根据索引获取数据
     */
    public T get(int position) {
        if (this.mDatas != null) {
            return this.mDatas.get(position);
        }
        return null;
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (this.mDatas != null) {
            this.mDatas.clear();
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除位置为position数据
     */
    public void delete(int position) {
        if (this.mDatas != null) {
            this.mDatas.remove(position);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 获取侧滑item的宽度
     *
     * @param holder   此item的holder
     * @param position 此item对应的位置
     * @return
     */
    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder, final int position) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            //侧滑有一个item
            if (viewGroup.getChildCount() == 2) {
                setOnClickOnItemView(viewGroup.getChildAt(1), position, mCallBackOne);
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
            //侧滑有两个item
            if (viewGroup.getChildCount() == 3) {
                setOnClickOnItemView(viewGroup.getChildAt(1), position, mCallBackOne);
                setOnClickOnItemView(viewGroup.getChildAt(2), position, mCallBackTwo);
                return viewGroup.getChildAt(1).getLayoutParams().width + viewGroup.getChildAt(2).getLayoutParams().width;
            }
        }
        return 0;
    }

    /**
     * 设置子view的点击事件
     *
     * @param view     子view
     * @param position 子view的位置
     * @param callBack 子view对应的回调事件
     */
    private void setOnClickOnItemView(View view, final int position, final SlideCallBack callBack) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mPosition = position - mHeadItemCount;
                if (mPosition >= 0 && mPosition < getItemCount()) {
                    if (callBack != null) {
                        callBack.onSlideClick(mPosition);
                    }
                }
            }
        });
    }

    /**
     * 获得recyclerview的子项的holder
     */
    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    /**
     * 获得recyclerview的子项
     */
    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }

    /**
     * 创建新View，被LayoutManager所调用
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createCusViewHolder(parent, mItemLayoutId);
    }

    /**
     * 将数据与界面进行绑定的操作
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (this.mDatas != null) {
            mPosition = position;
            convertViewHolder((BaseViewHolder) holder, this.mDatas.get(position));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if(this.mDatas!=null) {
            mPosition = position;
           if(payloads.isEmpty()){
               convertViewHolder((BaseViewHolder) holder , this.mDatas.get(position));
           }else {
               convertViewHolderOnChanged((BaseViewHolder) holder , this.mDatas.get(position),(String)payloads.get(0));
           }
        }
    }

    @Override
    public int getItemCount() {
        if (this.mDatas != null) {
            return this.mDatas.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return getDefItemViewType(position);
    }

    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public RecyclerView.ViewHolder createCusViewHolder(ViewGroup parent, int itemLayoutId) {
        BaseViewHolder viewHolder = BaseViewHolder.get(mContext, parent, itemLayoutId);
        return viewHolder;
    }

    /**
     * 转换ViewHolder抽象函数
     *
     * @param holder
     * @param t      数据对象
     */
    public abstract void convertViewHolder(BaseViewHolder holder, T t);

    public  void convertViewHolderOnChanged(BaseViewHolder holder , T t , String payLoad){

    }

    /**
     * 侧滑事件的回调接口
     */
    public interface SlideCallBack {
        void onSlideClick(int position);
    }
}
