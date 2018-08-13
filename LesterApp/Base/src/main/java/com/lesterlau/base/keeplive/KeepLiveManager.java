package com.lesterlau.base.keeplive;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.format.DateUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by liubin on 2017/10/31.
 */

public class KeepLiveManager {
    public static boolean debug = true;
    public static final int KEEPLIVE_NOTIFICATION_ID = 32;
    private static Set<Class> keepServices = new HashSet<>();

    /**
     * 添加service到保活
     *
     * @param cls
     */
    public static void addKeepLive(Class cls) {
        keepServices.add(cls);
        startKeepLiveService();
    }

    /**
     * 服务置于前台
     *
     * @param service
     */
    public static void startServiceForegroud(Service service) {
        Notification notification = new Notification();
        if (notification == null) {
            return;
        }
        service.startForeground(KEEPLIVE_NOTIFICATION_ID, notification);
    }

    /**
     * 启动需要保活的服务
     */
    public static void startNeedKeepLiveServices() {
        for (Class cls : keepServices) {
            if (!ServiceUtils.isServiceRunning(cls.getName())) {
                ServiceUtils.startService(cls);
            }
        }
    }

    /**
     * 启动保活服务
     */
    public static void startKeepLiveService() {
        if (!ServiceUtils.isServiceRunning(KeepLiveService.class.getName())) {
            ServiceUtils.startService(KeepLiveService.class);
            ServiceUtils.bindService(KeepLiveService.class, new KeepServiceConnection(), Context.BIND_AUTO_CREATE);
        }
    }

    public static class KeepServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startKeepLiveService();
        }
    }
}
