package com.lesterlau.base.keeplive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.NetworkUtils;


/**
 * Created by liubin on 2017/10/11.
 */

public class NetworkReceiver extends BroadcastReceiver {
    private CallBack callBack;

    public NetworkReceiver(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (callBack == null) {
                return;
            }
            if (NetworkUtils.isConnected()) {
                callBack.connectNetwork();
                if (NetworkUtils.isWifiConnected()) {
                    callBack.connectWifi();
                }
            } else {
                callBack.disConnectNetwork();
            }
        }

    }

    public interface CallBack {
        void connectNetwork();

        void connectWifi();

        void disConnectNetwork();
    }

}
