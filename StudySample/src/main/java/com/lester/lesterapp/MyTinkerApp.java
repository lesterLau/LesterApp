package com.lester.lesterapp;

import android.content.Intent;

import com.lester.lesterapp.receiver.MyReceiver;
import com.lester.lesterapp.service.MyService;
import com.lesterlau.base.tinker.BaseTinkerApplication;

public class MyTinkerApp extends BaseTinkerApplication {
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
    protected void registerRebootReceiver() {
        super.registerRebootReceiver();
        keepLiveReveiver = new MyReceiver();
        registerReceiver(keepLiveReveiver, rebootIntentFilter);
    }
}
