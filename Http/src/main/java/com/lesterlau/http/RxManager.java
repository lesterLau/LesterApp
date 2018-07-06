package com.lesterlau.http;


import rx.subscriptions.CompositeSubscription;

/**
 * Created by liubin on 2016/9/19.
 */
public class RxManager {

    private CompositeSubscription mCompositeSubscription = null;

    public RxManager(CompositeSubscription compositeDisposable) {
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