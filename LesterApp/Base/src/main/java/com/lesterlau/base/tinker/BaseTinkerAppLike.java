/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lesterlau.base.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.lesterlau.base.AppManager;
import com.lesterlau.base.keeplive.KeepLiveReveiver;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.ApplicationLifeCycle;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;


/**
 * tinker集成的第一种方式
 * 放弃使用APplication，初始化代码全部放入ApplicationLike里面
 * 需要在mainifest里面注册的时候填入ApplicationLike里面定义的Application
 * （如果需要继承BaseApplicationLike则需要重新定义Application）
 * because you can not use any other class in your application, we need to
 * move your implement of Application to {@link ApplicationLifeCycle}
 * As Application, all its direct reference class should be in the main dex.
 * <p>
 * We use tinker-android-anno to make sure all your classes can be patched.
 * <p>
 * application: if it is start with '.', we will add SampleApplicationLifeCycle's package name
 * <p>
 * flags:
 * TINKER_ENABLE_ALL: support dex, lib and resource
 * TINKER_DEX_MASK: just support dex
 * TINKER_NATIVE_LIBRARY_MASK: just support lib
 * TINKER_RESOURCE_MASK: just support resource
 * <p>
 * loaderClass: define the tinker loader class, we can just use the default TinkerLoader
 * <p>
 * loadVerifyFlag: whether check files' md5 on the load time, defualt it is false.
 * <p>
 * Created by liubin on 2017/10/11.
 */
public class BaseTinkerAppLike extends DefaultApplicationLike {
    private static final String TAG = BaseTinkerAppLike.class.getSimpleName();

    public BaseTinkerAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    protected static Application instance;
    protected KeepLiveReveiver keepLiveReveiver;
    protected IntentFilter rebootIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplication();
        registerRebootReceiver();
        AppManager.init(instance);
        LogUtils.d(TAG, "onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterRebootReceiver();
        LogUtils.d(TAG, "onTerminate");
    }

    protected void registerRebootReceiver() {
        rebootIntentFilter = new IntentFilter();
        rebootIntentFilter.addAction(Intent.ACTION_REBOOT);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        rebootIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
    }

    private void unregisterRebootReceiver() {
        if (keepLiveReveiver != null) {
            instance.unregisterReceiver(keepLiveReveiver);
        }
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        TinkerManager.setTinkerApplicationLike(this);

        TinkerManager.initFastCrashProtect();
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);

        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

}
