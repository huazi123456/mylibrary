package com.net;

import com.model.UcityMode;
import com.model.UserModel;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * 接口链接
 * Created by admin on 2017/9/5.
 */

public interface APIs {
    @GET("/users/{user}")
    Observable<UserModel> getUserInfo(@Path("user") String user);

    @GET("/easySupport")
    Observable<UcityMode> getUcity(@QueryMap Map<String, String> map);
}
