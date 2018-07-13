package com.lester.lesterapp.test.receiver;

import android.content.Context;

import com.blankj.utilcode.util.ServiceUtils;
import com.lesterlau.base.keeplive.BaseService;
import com.lesterlau.base.keeplive.KeepLiveReveiver;

public class MyReceiver extends KeepLiveReveiver {
    @Override
    protected void initService(Context context) {
        super.initService(context);
        ServiceUtils.startService(BaseService.class);
    }
}
