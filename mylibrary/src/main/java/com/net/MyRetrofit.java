package com.net;

import com.model.UcityMode;
import com.model.UserModel;

import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/9/5.
 */

public class MyRetrofit extends BaseRetrofit {

    private volatile static MyRetrofit instance = null;

    public MyRetrofit() {
        init();
    }

    public static MyRetrofit getInstance() {
        if (instance == null) {
            synchronized (MyRetrofit.class) {
                if (instance == null) {
                    instance = new MyRetrofit();
                }
            }
        }
        return instance;
    }

    public Observable<UserModel> getUserInfo(String user) {
        return mApis.getUserInfo(user).compose(RxUtil.<UserModel>scheduleRxHelper());
    }

    public Observable<UcityMode> getUcity(Map<String, String> map) {
        return mApis.getUcity(map).compose(RxUtil.<UcityMode>scheduleRxHelper());
    }
}
