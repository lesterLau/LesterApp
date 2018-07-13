package com.lester.lesterapp;

import android.app.Application;
import android.content.Intent;

import com.lester.lesterapp.test.receiver.MyReceiver;
import com.lester.lesterapp.test.service.MyService;
import com.lesterlau.base.tinker.BaseTinkerAppLike;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@DefaultLifeCycle(application = "com.lester.lesterapp.TinkerApp", flags = ShareConstants.TINKER_ENABLE_ALL, loadVerifyFlag = false)
public class MyAppLike extends BaseTinkerAppLike {
    public MyAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance.startService(new Intent(instance, MyService.class));
    }

    @Override
    protected void registerRebootReceiver() {
        super.registerRebootReceiver();
        keepLiveReveiver = new MyReceiver();
        instance.registerReceiver(keepLiveReveiver, rebootIntentFilter);
    }


}
