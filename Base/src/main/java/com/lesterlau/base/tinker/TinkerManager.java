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

import android.app.Application;
import android.content.Context;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.ApplicationLike;


/**
 * Created by liubin on 2017/10/11.
 */
public class TinkerManager {
    private static final String TAG = "Tinker.TinkerManager";

    private static ApplicationLike applicationLike;
    private static TinkerUncaughtExceptionHandler uncaughtExceptionHandler;
    private static boolean isInstalled = false;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static Application getApplication() {
        return applicationLike.getApplication();
    }

    public static Context getContext() {
        return applicationLike.getApplication();
    }

    public static void initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = new TinkerUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;
    }

    /**
     * app初始化
     */
    public static void init() {

    }
}
