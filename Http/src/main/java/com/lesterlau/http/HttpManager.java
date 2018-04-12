package com.lesterlau.http;


import rx.subscriptions.CompositeSubscription;

/**
 * Created by  lesterlau on 2018/4/9.
 */
public class HttpManager {

    private CompositeSubscription mCompositeSubscription = null;

    public HttpManager(CompositeSubscription compositeDisposable) {
        this.mCompositeSubscription = compositeDisposable;
    }

    /**
     * 取消请求
     */
    public void cancel() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}