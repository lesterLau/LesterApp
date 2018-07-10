package com.lester.lesterapp;

import android.app.Application;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ReflectUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.lesterlau.http.ApiErrorCode;
import com.lesterlau.http.HTCallBack;
import com.lesterlau.http.HttpResponse;
import com.lesterlau.http.RequestHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
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
    }
}
