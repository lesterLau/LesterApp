package com.lesterlau.base.keeplive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;


/**
 * Created by liubin on 2017/10/26.
 */

public class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");
        start();
        return START_STICKY;
    }

    private void start() {
        KeepLiveManager.startKeepLiveService();
        KeepLiveManager.startServiceForegroud(this);
        KeepLiveManager.addKeepLive(getClass());
    }

    @Override
    public void onDestroy() {
        LogUtils.d("onDestroy");
        ServiceUtils.startService(getClass());
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("onBind");
        start();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d("onUnbind");
        ServiceUtils.startService(getClass());
        return false;
    }
}
