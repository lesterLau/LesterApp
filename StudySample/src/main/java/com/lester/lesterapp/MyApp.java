package com.lester.lesterapp;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
