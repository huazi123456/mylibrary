package com.irecyclerview.stickyheadersrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irecyclerview.BaseViewHolder;
import com.irecyclerview.IRecyclerView;
import com.irecyclerview.RecycleBaseAdapter;
import com.mylibrary.R;

import java.util.List;

/**
 * Created by admin on 2017/9/15.
 */

public abstract class StickyRecyclerAdapter<T> extends RecycleBaseAdapter implements StickyRecyclerHeadersAdapter{

    private View mHeader;

    public StickyRecyclerAdapter(Context context) {
        super(context);
    }

    public StickyRecyclerAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public StickyRecyclerAdapter(Context context, int itemLayoutId, IRecyclerView iRecyclerView) {
        super(context, itemLayoutId, iRecyclerView);
    }

    public StickyRecyclerAdapter(Context context, int itemLayoutId, List datas) {
        super(context, itemLayoutId, datas);
    }

    @Override
    public long getHeaderId(int position) {
        if (position == 0) {
            return -1;
        } else {
            return (String.valueOf(get(position))).charAt(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
//        if (mHeader == null){
//            mHeader = new TextView(mContext);
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DimensUtil.pixelsToDp(mContext, 20));
//            mHeader.setLayoutParams(params);
//        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text, parent, false);
        return new RecyclerView.ViewHolder(view){};
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        textView.setTextColor(0xf5f5f500);
        textView.setText(String.valueOf(get(position)));
    }

    public abstract void convertHViewHolder(BaseViewHolder holder, T t);

    @Override
    public void convertViewHolder(BaseViewHolder holder, Object o) {
        convertHViewHolder(holder, (T) o);
    }
}
