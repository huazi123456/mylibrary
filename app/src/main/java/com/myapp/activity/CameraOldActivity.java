package com.myapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.camera.old.CameraSurfaceView;
import com.camera.old.CircleOnCamera;
import com.myapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CameraOldActivity extends Activity {

    @BindView(R.id.mCameraSurfaceView)
    CameraSurfaceView mCameraSurfaceView;//拍照显示界面

    @BindView(R.id.mIvShowPhoto)
    ImageView mIvShowPhoto;//拍完照显示图片

    @BindView(R.id.mRlTakePhoto)
    RelativeLayout mRlTakePhoto;//拍照时显示

    @BindView(R.id.mLlShowPhoto)
    LinearLayout mLlShowPhoto;//拍完照显示

    @BindView(R.id.mRectOnCamera)
    CircleOnCamera mCircleOnCamera;//焦点显示

    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_old_camera);
        ButterKnife.bind(this);
        initEvent();

    }

    private void initEvent() {
        mCameraSurfaceView.setBitmap(new CameraSurfaceView.CameraCall() {
            @Override
            public void show(Bitmap bitmap, boolean isVertical) {

                mLlShowPhoto.setVisibility(View.VISIBLE);
                mRlTakePhoto.setVisibility(View.INVISIBLE);
                mIvShowPhoto.setVisibility(View.VISIBLE);

                if (isVertical) {
                    mIvShowPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    mIvShowPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                mIvShowPhoto.setImageBitmap(bitmap);
            }

            @Override
            public boolean hasFind(float x, float y) {
                if (y > mRlTakePhoto.getTop()) {
                    return false;
                }
                if (isShow)
                    mCircleOnCamera.update();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraSurfaceView.registerSensorManager(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraSurfaceView.unregisterSensorManager(this);
    }

    @OnClick({R.id.mIvTakePhoto, R.id.mRlBack, R.id.mIvTakeBack, R.id.mIvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mIvTakePhoto:
                isShow = false;
                mCameraSurfaceView.takePicture();
                break;
            case R.id.mRlBack:
                finish();
                break;
            case R.id.mIvTakeBack:
                isShow = true;
                mLlShowPhoto.setVisibility(View.INVISIBLE);
                mRlTakePhoto.setVisibility(View.VISIBLE);
                mIvShowPhoto.setVisibility(View.INVISIBLE);
                break;
            case R.id.mIvConfirm:
                finish();
                break;
        }
    }

}
