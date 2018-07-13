package com.lester.lesterapp.test.service;

import com.lesterlau.base.keeplive.BaseService;

public class MyService extends BaseService {
    @Override
    public void onCreate() {
        super.onCreate();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while (true) {
//                        Thread.sleep(1000);
//                        String s = "Tinker生效了\n" + "现在时间是：" + TimeUtils.date2String(new Date());
//                        ToastUtils.showShort(s);
//                        LogUtils.e("123", s);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}
