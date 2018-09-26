package com.lesterlau.http;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 网络请求管理类，基于Request对retrofit的二次封装
 * Created by liubin on 2016/9/19.
 */
public class RequestHelper {
    private static final int CONNECT_TIMEOUT = 5;  //请求超时时间，单位：min
    private static RequestHelper instance;
    private static RequestAPI requestAPI;
    private Interception interception;
    private static Map<String, RequestAPI> requestApiCache = Collections.synchronizedMap(new HashMap<String, RequestAPI>());
    private static Map<String, HttpRequest> httpRequestCache = Collections.synchronizedMap(new HashMap<String, HttpRequest>());

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        return getInstance(getBaseUrl());
    }

    public static RequestHelper getInstance(String url) {
        requestAPI = requestApiCache.get(url);
        if (requestAPI == null) {
            init(url);
        }
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
    private static synchronized void init(String baseUrl) {
        OkHttpClient.Builder localBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            localBuilder.addInterceptor(loggingInterceptor);
        }

//        /**
//         * 在建立retrofit实例前，调用设置证书的方法即可
//         */
//        try {
//            setCertificates(localBuilder,MyApplication.getContext().getAssets().open("tainiu.cer"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        localBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("Accept", "application/json")
                        .addHeader("Connection", "keep-alive")
                        .build();
                return chain.proceed(request);
            }
        });

        try {
            //设置超时
            localBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES);
            localBuilder.readTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES);
            localBuilder.writeTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES);
            //错误重连
            localBuilder.retryOnConnectionFailure(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(localBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            requestAPI = retrofit.create(RequestAPI.class);
            requestApiCache.put(baseUrl, requestAPI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkInit(HttpRequest request) {
        if (requestAPI == null) {
            throw new RuntimeException("RequestHelper must be init,Please call init method.");
        }
        if (request == null) {
            throw new RuntimeException("request is null.");
        }

    }

    public void get(HttpRequest request) {
        checkInit(request);
        request.method = HttpRequest.GET;
        Subscription subscription;
        if (request.params == null) {
            subscription = requestAPI.executeGetCall(request.url)
                    // Run on a background thread
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySuccess(request), new MyError(request));
        } else {
            RequestBody body = toRequestBody(request.params);
            subscription = requestAPI.executeGetCall(request.url, body)
                    // Run on a background thread
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySuccess(request), new MyError(request));
        }
        RxManager.getInstance().add(request.tag, subscription);
        if (request.showLoading) {
            CustomProgressDialog.showProgressDialog(request.context, request.canCancel, request.canceledOnTouchOutside);
        }
    }

    public void post(HttpRequest request) {
        checkInit(request);
        request.method = HttpRequest.POST;
        Subscription subscription;
        if (request.params == null) {
            subscription = requestAPI.executePostCall(request.url)
                    // Run on a background thread
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySuccess(request), new MyError(request));
        } else {
            RequestBody body = toRequestBody(request.params);
            subscription = requestAPI.executePostCall(request.url, body)
                    // Run on a background thread
                    .subscribeOn(Schedulers.io())
                    // Be notified on the main thread
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySuccess(request), new MyError(request));
        }
        RxManager.getInstance().add(request.tag, subscription);
        if (request.showLoading) {
            CustomProgressDialog.showProgressDialog(request.context, request.canCancel, request.canceledOnTouchOutside);
        }
    }

    private RequestBody toRequestBody(Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
    }

    private static String toRequestTag(String url, Map<String, Object> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        return url + map.toString();
    }

    private RequestBody toDesRequestBody(String des) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), des);
    }

    private MultipartBody.Part toMultipartBody(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        return MultipartBody.Part.createFormData("file", file.getName().trim(), requestFile);
    }

    private static String getBaseUrl() {
        String url = SPUtils.getInstance().getString(ApiErrorCode.HOST_KEY.getValue());
        if (!TextUtils.isEmpty(url)) {
            return url;
        }
        return Hosts.HOST_URL;
    }

    public void setInterception(Interception interception) {
        this.interception = interception;
    }

    public interface Interception {
        public boolean intercept(HttpRequest httpRequest, Response<ResponseBody> responseBodyResponse);

    }


    /**
     * 通过okhttpClient来设置证书
     *
     * @param clientBuilder OKhttpClient.builder
     * @param certificates  读取证书的InputStream
     */
    public static void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) certificate.close();
                } catch (IOException e) {
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyError implements Action1<Throwable> {
        private HttpRequest httpRequest;

        public MyError(HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public void call(Throwable throwable) {
            LogUtils.e(throwable);
            if (httpRequest != null && httpRequest.cb != null) {
                //默认code和msg
                int errorCode = ApiErrorCode.ERROR_UNKNOWN.getCode();
                String errorMsg = ApiErrorCode.ERROR_UNKNOWN.getValue();
                if (!NetworkUtils.isConnected()) {//无网络失败
                    errorCode = ApiErrorCode.ERROR_NETWORK.getCode();
                    errorMsg = ApiErrorCode.ERROR_NETWORK.getValue();
                } else if (throwable instanceof UnknownHostException) {
                    errorCode = ApiErrorCode.ERROR_HOST.getCode();
                    errorMsg = ApiErrorCode.ERROR_HOST.getValue();
                } else if (throwable instanceof IOException) {
                    errorCode = ApiErrorCode.ERROR_IO.getCode();
                    errorMsg = ApiErrorCode.ERROR_IO.getValue();
                } else if (throwable instanceof SocketTimeoutException) {
                    errorCode = ApiErrorCode.ERROR_CONNECTION_TIMEOUT.getCode();
                    errorMsg = ApiErrorCode.ERROR_CONNECTION_TIMEOUT.getValue();
                }
                httpRequest.cb.onError(new ApiException(errorCode, errorMsg));
                ToastUtils.showShort(errorMsg);
            }
            RxManager.getInstance().remove(httpRequest.tag);
            CustomProgressDialog.stopProgressDialog();
        }
    }

    class MySuccess implements Action1<Response<ResponseBody>> {
        private HttpRequest httpRequest;

        public MySuccess(HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public void call(Response<ResponseBody> responseBodyResponse) {
            LogUtils.d(responseBodyResponse);
            boolean intercept = false;
            if (interception != null) {
                intercept = interception.intercept(httpRequest, responseBodyResponse);
            }
            if (!intercept && httpRequest != null && httpRequest.cb != null) {
                boolean onSuccess = false;
                int errorCode = ApiErrorCode.ERROR_UNKNOWN.getCode();
                String errorMsg = ApiErrorCode.ERROR_UNKNOWN.getValue();
                if (responseBodyResponse.isSuccessful()) {
                    try {
                        String result = responseBodyResponse.body().string();
                        LogUtils.d(result);
                        Type t = ((ParameterizedType) httpRequest.cb.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        HttpResponse response = new Gson().fromJson(result, t);
                        if (response.isResult() || response.getCode() == 200 || response.getErrorCode() >= 0) {
                            httpRequest.cb.onSuccess(response);
                            onSuccess = true;
                        } else {
                            errorCode = response.getCode();
                            errorMsg = response.getMsg();
                        }
                    } catch (IOException e) {
                        errorCode = ApiErrorCode.ERROR_IO.getCode();
                        errorMsg = ApiErrorCode.ERROR_IO.getValue();
                    }
                } else {
                    errorCode = responseBodyResponse.code();
                    errorMsg = responseBodyResponse.message();
                }
                if (!onSuccess) {
                    httpRequest.cb.onError(new ApiException(errorCode, errorMsg));
                    ToastUtils.showShort(errorMsg);
                }
            }
            if (httpRequest.retry) {
                RxManager.getInstance().remove(httpRequest.tag);
            }
            CustomProgressDialog.stopProgressDialog();
        }
    }

    public static class HttpRequest {
        public static final String GET = "get";
        public static final String POST = "post";
        public String method;//方法
        public boolean retry;//重试
        public String baseUrl;//域名
        public String url;//地址
        public Map<String, Object> params;//参数
        public HTCallBack cb;//回调
        public String tag;//标签
        public Context context;//上下文，如果需要显示loading 必须传Activity的Context
        public boolean showLoading;//是否显示loading
        public boolean canCancel;//是否可以点击back健消失
        public boolean canceledOnTouchOutside;//是否可以点击外部消失

        public HttpRequest(HttpRequest request) {
            this(request.retry, request.baseUrl, request.url, request.params, request.cb, request.tag, request.context, request.showLoading, request.canCancel, request.canceledOnTouchOutside);
        }

        public HttpRequest(String url, HTCallBack cb) {
            this(url, null, cb, null);
        }

        public HttpRequest(String url, Map<String, Object> params, HTCallBack cb) {
            this(url, params, cb, null);
        }

        public HttpRequest(String url, HTCallBack cb, Context context) {
            this(url, null, cb, context);
        }

        public HttpRequest(String url, Map<String, Object> params, HTCallBack cb, Context context) {
            this(true, getBaseUrl(), url, params, cb, toRequestTag(url, params), context, context instanceof Activity, context instanceof Activity, false);
        }

        public HttpRequest(boolean retry, String baseUrl, String url, Map<String, Object> params, HTCallBack cb, String tag, Context context, boolean showLoading, boolean canCancel, boolean canceledOnTouchOutside) {
            this.retry = retry;
            this.baseUrl = baseUrl;
            this.url = url;
            this.params = params;
            this.cb = cb;
            this.tag = tag;
            this.context = context;
            this.showLoading = showLoading;
            this.canCancel = canCancel;
            this.canceledOnTouchOutside = canceledOnTouchOutside;
        }
    }
}

