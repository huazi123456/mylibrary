package com.myapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.base.BaseAppCompatActivity;
import com.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 2017/9/1.
 */

public class KeepActivity extends BaseAppCompatActivity {


    @BindView(R.id.mIv1)
    ImageView mIv1;
    @BindView(R.id.mIv2)
    ImageView mIv2;
    @BindView(R.id.mIv3)
    ImageView mIv3;
    @BindView(R.id.mIv4)
    ImageView mIv4;
    @BindView(R.id.mIv5)
    ImageView mIv5;
    @BindView(R.id.mIv6)
    ImageView mIv6;
    @BindView(R.id.mTable)
    TableLayout mTable;

    private ImageView[] ivs;
    private int mPoint = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_keep, R.layout.title_toolbar);
        ButterKnife.bind(this);
        setTitle("屏保");

        ivs = new ImageView[]{mIv1, mIv2, mIv3, mIv6, mIv5, mIv4};
        mHandler = new Handler();
        mTable.setVisibility(View.VISIBLE);
        mHandler.post(mPointsLoop);
    }

    private Runnable mPointsLoop = new Runnable() {
        @Override
        public void run() {
            showPic(mPoint);
            mPoint++;
            mPoint = mPoint % 6;
            mHandler.postDelayed(mPointsLoop, 2500);
        }
    };

    private void showPic(int n) {
        for (int i = 0; i < ivs.length; i++) {
            ivs[i].setVisibility(View.INVISIBLE);
            if (n == i) {
                ivs[n].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mPointsLoop);
        mHandler = null;
    }
}
