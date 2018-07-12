package com.lesterlau.base.tinker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lesterlau.base.keeplive.KeepLiveReveiver;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by liubin on 2017/10/31.
 */

public class BaseTinkerApplication extends TinkerApplication {
    protected static Application instance;
    protected KeepLiveReveiver keepLiveReveiver;
    protected IntentFilter rebootIntentFilter;

    protected BaseTinkerApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.lesterlau.base.tinker.BaseTinkerAppLike");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);
        LogUtils.d("onCreate");
        Bugly.init(getApplicationContext(), "310366dc88", true);
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
