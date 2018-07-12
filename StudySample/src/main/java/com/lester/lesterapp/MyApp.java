package com.lester.lesterapp;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lester.lesterapp.receiver.MyReceiver;
import com.lester.lesterapp.service.MyService;
import com.lesterlau.base.BaseApplication;
import com.tencent.bugly.beta.Beta;

public class MyApp extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
//        RequestHelper.getInstance().setInterception(new RequestHelper.Interception() {
//            @Override
//            public boolean intercept(RequestHelper.HttpRequest httpRequest, Response<ResponseBody> responseBodyResponse) {
//                if (httpRequest.retry) {
//                    RequestHelper.getInstance().get(httpRequest);
//                    httpRequest.retry = false;
//                }
//                return httpRequest.retry;
//            }
//        });
        startService(new Intent(this, MyService.class));
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Utils.init(this);
        //使用腾讯bugly热更新
        LogUtils.d("Beta attachBaseContext", "attachBaseContext");
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    @Override
    protected void registerRebootReceiver() {
        super.registerRebootReceiver();
        keepLiveReveiver = new MyReceiver();
        registerReceiver(keepLiveReveiver, rebootIntentFilter);
    }

    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

}
