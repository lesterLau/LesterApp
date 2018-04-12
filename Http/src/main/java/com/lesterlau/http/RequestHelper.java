package com.lesterlau.http;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscription;
import rx.android.BuildConfig;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 网络请求管理类，基于Request对retrofit的二次封装
 * Created by  lesterlau on 2018/4/9.
 */
public class RequestHelper {
    private static final int CONNECT_TIMEOUT = 5;  //请求超时时间，单位：min
    private static final int RW_TIMEOUT = 5;       //读写超时时间，单位：min
    private static RequestHelper instance;
    private static RequestAPI requestAPI;
    private Interception interception;

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        if (instance == null) {
            synchronized (RequestHelper.class) {
                if (instance == null) {
                    instance = new RequestHelper();
                }
            }
        }
        return instance;
    }


    /**
     * 构造retrofit请求api
     *
     * @return retrofit请求api
     */
    private static void init(String baseUrl) {
        OkHttpClient.Builder localBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            localBuilder.addInterceptor(loggingInterceptor);
        }
        //设置超时
        localBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES);
        localBuilder.readTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
        localBuilder.writeTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
        //错误重连
        localBuilder.retryOnConnectionFailure(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(localBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .build();
        requestAPI = retrofit.create(RequestAPI.class);
    }

    private void checkInit() {
        if (requestAPI == null) {
            throw new RuntimeException("RequestHelper must be init,Please call init methoc.");
        }
    }


    public HttpManager get(String url, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executeGetCall(url)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new HttpManager(mCompositeSubscription);
    }

    public HttpManager get(String url, Map<String, Object> params, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executeGetCall(url, toRequestBody(params))
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new HttpManager(mCompositeSubscription);
    }

    public HttpManager post(String url, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executePostCall(url)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new HttpManager(mCompositeSubscription);
    }

    public HttpManager post(String url, Map<String, Object> params, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executePostCall(url, toRequestBody(params))
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new HttpManager(mCompositeSubscription);
    }


    class MyAction implements Action1<Response<ResponseBody>> {
        private HTCallBack cb;
        private CompositeSubscription mCompositeSubscription;

        public MyAction(HTCallBack cb, CompositeSubscription mCompositeSubscription) {
            this.cb = cb;
            this.mCompositeSubscription = mCompositeSubscription;
        }

        @Override
        public void call(Response<ResponseBody> responseBodyResponse) {
            if (interception != null) {
                interception.intercept(responseBodyResponse);
            }
            if (responseBodyResponse.isSuccessful()) {
                if (cb != null) {
                    try {
                        String result = responseBodyResponse.body().string();
                        Type t = ((ParameterizedType) cb.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        cb.onSuccess(new Gson().fromJson(result, t));
                    } catch (IOException e) {
                        LogUtils.e("MyAction", e);
                    }
                }
            } else {
                if (cb != null) {
                    int code = responseBodyResponse.code();
                    String msg;
                    try {
                        msg = responseBodyResponse.errorBody().string();
                    } catch (Exception e) {
                        try {
                            msg = responseBodyResponse.body().string();
                        } catch (IOException e1) {
                            msg = responseBodyResponse.message();
                        }
                    }
                    cb.onError(new ApiException(code, msg));
                }
            }
            if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
                mCompositeSubscription.unsubscribe();
            }
        }
    }

    private RequestBody toRequestBody(Map<String, Object> map) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
    }

    public interface Interception {
        public boolean intercept(Response<ResponseBody> responseBodyResponse);
    }
}
