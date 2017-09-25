package com.camera.old;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.camera.util.AngleUtil;
import com.camera.util.CameraParamUtil;
import com.camera.util.FileUtil;
import com.camera.util.ScreenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Bitmap.createBitmap;
import static android.support.v4.math.MathUtils.clamp;

/**
 * 相机拍照界面
 * Created by xyh on 2017/9/20
 */
@SuppressWarnings("deprecation")
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;
    private float screenProp = 0f;
    private int angle = 0;//相机每时每刻的角度
    private int cameraAngle = 90;//摄像头角度   默认为90度
    private int nowAngle;//拍照时的相机角度
    private SensorManager sm = null;
    private Camera.Parameters mParams;//拍照参数
    private int handlerTime;//对焦处理次数
    private float mWidth;
    private float mHeight;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        cameraAngle = CameraParamUtil.getInstance().getCameraDisplayOrientation(mContext, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机
            mParams = mCamera.getParameters();
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("huazi", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //设置参数并开始预览
        setParams();

        //自动对焦
        postDelayed(new Runnable() {
            @Override
            public void run() {
                handFoucs(mWidth / 2, mHeight / 2);
            }
        }, 500);
        Log.i("huazi", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();//停止预览
        mCamera.release();//释放相机资源
        mCamera = null;
        holder = null;

        Log.i("huazi", "surfaceDestroyed");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = mWidth = getMeasuredWidth();
        float heightSize = mHeight = getMeasuredHeight();
        if (screenProp == 0) {
            screenProp = heightSize / widthSize;
        }

        Log.i("huazi", "onMeasure");
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }
            float[] values = event.values;
            angle = AngleUtil.getSensorAngle(values[0], values[1]);
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            mCamera.stopPreview();// 关闭预览

            switch (cameraAngle) {
                case 90:
                    nowAngle = Math.abs(angle + cameraAngle) % 360;
                    break;
                case 270:
                    nowAngle = Math.abs(cameraAngle - angle);
                    break;
            }

            Log.i("huazi", angle + " = " + cameraAngle + " = " + nowAngle);

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            matrix.setRotate(nowAngle);
            bitmap = createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            FileUtil.saveBitmap("Pictures", bitmap);
            mCamera.startPreview();// 开启预览

            if (mCameraCall != null) {
                if (nowAngle == 90 || nowAngle == 270) {
                    mCameraCall.show(bitmap, true);
                } else {
                    mCameraCall.show(bitmap, false);
                }
            }

        }
    };

    public void takePicture() {
        mCamera.takePicture(null, null, pictureCallback);
    }

    private void setParams() {
        try {
            mParams = mCamera.getParameters();
            Camera.Size previewSize = CameraParamUtil.getInstance().getPreviewSize(mParams
                    .getSupportedPreviewSizes(), 1000, screenProp);
            Camera.Size pictureSize = CameraParamUtil.getInstance().getPictureSize(mParams
                    .getSupportedPictureSizes(), 1200, screenProp);

            mParams.setPreviewSize(previewSize.width, previewSize.height);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);

            if (CameraParamUtil.getInstance().isSupportedFocusMode(
                    mParams.getSupportedFocusModes(),
                    Camera.Parameters.FOCUS_MODE_AUTO)) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (CameraParamUtil.getInstance().isSupportedPictureFormats(mParams.getSupportedPictureFormats(),
                    ImageFormat.JPEG)) {
                mParams.setPictureFormat(ImageFormat.JPEG);
                mParams.setJpegQuality(100);
            }
            mCamera.cancelAutoFocus();
            mCamera.setParameters(mParams);
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(cameraAngle);//浏览角度
            mCamera.startPreview();//启动浏览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerSensorManager(Context context) {
        if (sm == null) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager
                .SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorManager(Context context) {
        if (sm == null) {
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        sm.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    if (mCameraCall != null) {
                        float x = event.getX();
                        float y = event.getY();
                        if (mCameraCall.hasFind(x, y)) {
                            handFoucs(x, y);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void handFoucs(final float x, final float y) {
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机;
        }
        final Camera.Parameters params = mCamera.getParameters();
        Rect focusRect = calculateTapArea(x, y, 1f, mContext);
        mCamera.cancelAutoFocus();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            return;
        }
        final String currentFocusMode = params.getFocusMode();
        try {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(params);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success || handlerTime > 10) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(currentFocusMode);
                        camera.setParameters(params);
                        handlerTime = 0;
                    } else {
                        handlerTime++;
                        handFoucs(x, y);
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private Rect calculateTapArea(float x, float y, float coefficient, Context context) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / ScreenUtils.getScreenHeight(context) * 2000 - 1000);
        int centerY = (int) (y / ScreenUtils.getScreenWidth(context) * 2000 - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF
                .bottom));
    }

    private CameraCall mCameraCall;

    public void setBitmap(CameraCall cameraCall) {
        mCameraCall = cameraCall;
    }

    public interface CameraCall {
        void show(Bitmap bitmap, boolean isVertical);

        boolean hasFind(float x, float y);
    }

}
