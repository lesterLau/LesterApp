package com.lesterlau.base.mvvm.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public abstract class BaseViewModel extends BaseObservable {
    protected Activity activity;

    public BaseViewModel(Activity activity) {
        this.activity = activity;
        loadData();
    }

    public abstract void loadData();

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
