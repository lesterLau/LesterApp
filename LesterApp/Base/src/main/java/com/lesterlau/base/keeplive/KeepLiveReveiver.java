package com.lesterlau.base.keeplive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;


/**
 * Created by liubin on 2017/10/27.
 */

public class KeepLiveReveiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("KeepLiveReveiver action=" + intent.getAction());
        if (Intent.ACTION_REBOOT.equals(intent.getAction())) {
            initService(context);
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            startKeepLiveActivity();
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            finishKeepLiveActivity();
        }
        rebootService();
    }

    private void finishKeepLiveActivity() {
        ActivityUtils.finishActivity(KeepLiveActivity.class);
    }

    private void startKeepLiveActivity() {
        ActivityUtils.startActivity(KeepLiveActivity.class);
    }

    /**
     * 初始化服务，监听到重启广播时调用
     *
     * @param context
     */
    protected void initService(Context context) {

    }

    /**
     * 重启保活服务
     **/
    protected void rebootService() {
        KeepLiveManager.startKeepLiveService();
    }
}
