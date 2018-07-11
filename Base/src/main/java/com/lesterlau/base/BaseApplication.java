package com.lesterlau.base;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lesterlau.base.keeplive.KeepLiveReveiver;

/**
 * Created by liubin on 2017/10/31.
 */

public class BaseApplication extends Application {
    protected static Application instance;
    protected KeepLiveReveiver keepLiveReveiver;
    protected IntentFilter rebootIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);
        LogUtils.d("onCreate");
        registerRebootReceiver();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterRebootReceiver();
        LogUtils.d("onTerminate");
    }

    protected void registerRebootReceiver() {
        rebootIntentFilter = new IntentFilter();
        rebootIntentFilter.addAction(Intent.ACTION_REBOOT);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    private void unregisterRebootReceiver() {
        if (keepLiveReveiver != null) {
            unregisterReceiver(keepLiveReveiver);
        }
    }

    public static Application getInstance() {
        return instance;
    }
}
