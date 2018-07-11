package com.lester.lesterapp;

import android.content.Intent;

import com.lester.lesterapp.receiver.MyReceiver;
import com.lesterlau.base.BaseApplication;
import com.lesterlau.base.keeplive.BaseService;

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
        startService(new Intent(this, BaseService.class));
    }

    @Override
    protected void registerRebootReceiver() {
        super.registerRebootReceiver();
        keepLiveReveiver = new MyReceiver();
        registerReceiver(keepLiveReveiver, rebootIntentFilter);
    }
}
