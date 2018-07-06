package com.lesterlau.base;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by liubin on 2017/10/31.
 */

public class BaseApplication extends Application {
    private static Application instance;
    //    protected RebootServiceReveiver rebootServiceReveiver;
    protected IntentFilter rebootIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("onCreate");
        registerRebootReceiver();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterRebootReceiver();
        LogUtils.i("onTerminate");
    }

    protected void registerRebootReceiver() {
        rebootIntentFilter = new IntentFilter();
        rebootIntentFilter.addAction(Intent.ACTION_TIME_TICK);
        rebootIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        rebootIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        rebootIntentFilter.addAction(Intent.ACTION_USER_PRESENT);
        rebootIntentFilter.addAction("com.tencent.mm.plugin.report.service.KVCommCrossProcessReceiver");
        rebootIntentFilter.addAction("com.tencent.mm.ui.ACTION_ACTIVE");
        rebootIntentFilter.addAction("com.tencent.mm.ui.ACTION_DEACTIVE");
        rebootIntentFilter.addAction("com.tencent.tmsdk.HeartBeatPlot.ACTION_HEARTBEAT_PLOT_ALARM_CYCLE");
        rebootIntentFilter.addAction("com.tencent.mm.plugin.report.service.KVCommCrossProcessReceiver");
        rebootIntentFilter.addAction("com.tencent.mm.Intent.ACTION_CLICK_FLOW_REPORT");
    }

    private void unregisterRebootReceiver() {
//        if (rebootServiceReveiver != null) {
//            unregisterReceiver(rebootServiceReveiver);
//        }
    }

    public static Application getInstance() {
        return instance;
    }
}
