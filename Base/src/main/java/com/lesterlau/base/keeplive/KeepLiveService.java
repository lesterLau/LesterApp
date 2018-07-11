package com.lesterlau.base.keeplive;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;


/**
 * Created by liubin on 2017/10/26.
 */

public class KeepLiveService extends BaseService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("onStartCommand");
        KeepLiveManager.startNeedKeepLiveServices();
        KeepLiveManager.startServiceForegroud(this);
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d("onUnbind");
        ServiceUtils.startService(getClass());
        return false;
    }
}
