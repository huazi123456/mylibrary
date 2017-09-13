package com.myapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.base.BaseAppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.model.UserModel;
import com.myapp.R;
import com.net.MyRetrofit;
import com.net.MySubscriber;
import com.picture.glide.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by xyh on 2017/9/4.
 */

public class GlideActivity extends BaseAppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    private RequestManager glideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_glide, R.layout.title_toolbar);
        ButterKnife.bind(this);
        setTitle("图片控件glide");
        glideRequest = Glide.with(this);
        //glideRequest.load("https://www.baidu.com/img/bdlogo.png").transform(new GlideRoundTransform(this, 30)).into(imageView);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                glideRequest.load("https://www.baidu.com/img/bdlogo.png").transform(new GlideCircleTransform(GlideActivity.this)).into(imageView);
            }
        }, 1000);

    }

    /**
     * 创建通知小米系统设置桌面icon数字角标
     * 官网文档
     * @param context
     * @param title
     * @param content
     * @param number
     */
    public static void sendIconNumber(Context context, String title, String content, int number) {
//        NotificationManager mNotificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Notification.Builder builder = new Notification.Builder(context)
//                .setContentTitle(title).setContentText(content).setSmallIcon(R.mipmap.ic_launcher);
//        //设置通知点击后意图
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//
//        try {
//            Field field = notification.getClass().getDeclaredField("extraNotification");
//            Object extraNotification = field.get(notification);
//            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
//            method.invoke(extraNotification, number);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mNotificationManager.notify(0, notification);
    }


    @OnClick(R.id.imageView)
    public void onViewClicked() {
        MyRetrofit.getInstance().getUserInfo("Guolei1130").subscribe(new MySubscriber<UserModel>() {
            @Override
            protected void onFail(Throwable e) {
                Log.i("huazi",e.getMessage().toString());
            }

            @Override
            protected void onSuccess(UserModel userModel) {
                Log.i("huazi",userModel.toString());
            }
        });
    }
}
