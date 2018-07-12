package com.lester.lesterapp.service;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lesterlau.base.keeplive.BaseService;

import java.util.Date;

public class MyService extends BaseService {
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        String s = "Tinker生效了\n" + "现在时间是：" + TimeUtils.date2String(new Date());
                        ToastUtils.showShort(s);
                        LogUtils.e("123", s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
