package com.myapp;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.BaseRecyclerActivity;
import com.irecyclerview.BaseViewHolder;
import com.irecyclerview.IRecyclerView;
import com.irecyclerview.RecycleBaseAdapter;
import com.myapp.model.ActivityModel;
import com.utils.OrientationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseRecyclerActivity<IRecyclerView> {

    @BindView(R.id.mIRecyclerView)
    IRecyclerView mIRecyclerView;

    List<ActivityModel> mList = new ArrayList<>();
    private OrientationUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState, R.layout.layout_irecyclerview, R.layout.title_toolbar);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    @Override
    protected void initView() {
        setTitle("主页");
        super.initView();
        utils = new OrientationUtils(this, SensorManager.SENSOR_DELAY_NORMAL);
        if (utils.canDetectOrientation()){
            utils.enable();
        }
    }

    @Override
    protected void setRecyclerView() {
        mRecyclerView = mIRecyclerView;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new RecycleBaseAdapter<ActivityModel>(this, R.layout.item_avtivity_name) {
            @Override
            public void convertViewHolder(BaseViewHolder holder, final ActivityModel model) {
                holder.setText(R.id.mActivityName, model.getName());
                holder.setOnClickListener(R.id.mAnItem, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(MainActivity.this, Class.forName("com.myapp.activity." + model.getName()));
                            startActivity(intent, false);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        //startActivity(new Intent(MainActivity.this, CameraTwoActivity.class));
                    }
                });
            }
        };
        mIRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initHeader() {
//        View header = LayoutInflater.from(this).inflate(R.layout.item_avtivity_name, (ViewGroup) findViewById(android.R.id.content), false);
//        mIRecyclerView.addHeaderView(header);
    }

    @Override
    protected void setLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mIRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void setEmptyView() {
//        mEmptyView = LayoutInflater.from(this).inflate(R.layout.item_avtivity_name, (ViewGroup) findViewById(android.R.id.content), false);
//        mIRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    protected void getData() {
        ActivityModel model = new ActivityModel();
        model.setName("KeepActivity");
        mList.add(model);

//        model = new ActivityModel();
//        model.setName("GlideActivity");
//        mList.add(model);

        model = new ActivityModel();
        model.setName("MediaActivity");
        mList.add(model);

//        model = new ActivityModel();
//        model.setName("StickyActivity");
//        mList.add(model);

        model = new ActivityModel();
        model.setName("CameraOldActivity");
        mList.add(model);

        model = new ActivityModel();
        model.setName("StartCameraActivity");
        mList.add(model);

        executeOnLoadDataSuccess(mList, mList.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        utils.disable();
    }
}
