package com.net;

import rx.Subscriber;

/**
 * Created by admin on 2017/9/5.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFail(e);
    }

    /**
     * 回调成功
     */
    protected abstract void onSuccess(T t);

    /**
     * 回调失败
     */
    protected abstract void onFail(Throwable e);


}
