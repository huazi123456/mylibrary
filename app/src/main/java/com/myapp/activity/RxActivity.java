package com.myapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.base.BaseAppCompatActivity;
import com.model.UserModel;
import com.myapp.R;
import com.net.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by admin on 2017/9/27.
 */

public class RxActivity extends BaseAppCompatActivity {

    @BindView(R.id.mText)
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_rx, R.layout.title_toolbar);
        ButterKnife.bind(this);
        setTitle("RxJava");
    }

    private void rx1() {
        final int[] i = {1};
        Observable
                .interval(1, TimeUnit.SECONDS)
//                .create(new Observable.OnSubscribe<String>() {
//                    @Override
//                    public void call(Subscriber<? super String> subscriber) {
//                        subscriber.onNext("hello,rxjava" + i[0]);
//                        i[0]++;
//                    }
//                })

                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return aLong = aLong + 2;
                    }
                })

                .takeUntil(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long integer) {
                        return integer == 6;
                    }
                })

                .compose(RxUtil.<Long>scheduleRxHelper())
                .subscribe();
    }

    private void rx2() {
        Observable.just("hi,rxjava", "hello,rxjava")
                .compose(RxUtil.<java.lang.String>scheduleRxHelper())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mText.setText(s);
                    }
                });
    }

    private void rx3() {
        Observable.create(new Observable.OnSubscribe<List<UserModel>>() {
            @Override
            public void call(Subscriber<? super List<UserModel>> subscriber) {
                List<UserModel> list = new ArrayList<>();
                UserModel model;
                for (int i = 0; i < 5; i++) {
                    model = new UserModel();
                    model.setId("" + i);
                    model.setLogin("user" + i);
                    list.add(model);
                }
                subscriber.onNext(list);
            }
        })
                .map(new Func1<List<UserModel>, List<UserModel>>() {
                    @Override
                    public List<UserModel> call(List<UserModel> userModels) {
                        userModels.get(3).setLogin("hello");
                        return userModels;
                    }
                })

                .flatMap(new Func1<List<UserModel>, Observable<UserModel>>() {
                    @Override
                    public Observable<UserModel> call(List<UserModel> userModels) {
                        return Observable.from(userModels);
                    }
                })
                .filter(new Func1<UserModel, Boolean>() {
                    @Override
                    public Boolean call(UserModel userModel) {
                        return userModel.getId().equals("3");
                    }
                })
                .compose(RxUtil.<UserModel>scheduleRxHelper())
                .subscribe(new Subscriber<UserModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        mText.setText(userModel.getLogin());
                    }
                });
    }

    @OnClick({R.id.mRx1, R.id.mRx2, R.id.mRx3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRx1:
                rx1();
                break;
            case R.id.mRx2:
                rx2();
                break;
            case R.id.mRx3:
                rx3();
                break;
        }
    }
}
