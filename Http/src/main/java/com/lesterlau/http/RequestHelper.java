package com.lesterlau.http;


import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 网络请求管理类，基于Request对retrofit的二次封装
 * Created by liubin on 2016/9/19.
 */
public class RequestHelper {
    private static final int CONNECT_TIMEOUT = 5;  //请求超时时间，单位：min
    private static final int RW_TIMEOUT = 5;       //读写超时时间，单位：min
    private static RequestHelper instance;
    private static RequestAPI requestAPI;
    private Interception interception;
    private static Map<String, RequestAPI> requestApiCache = Collections.synchronizedMap(new HashMap<String, RequestAPI>());

    private RequestHelper() {
    }

    public static RequestHelper getInstance() {
        return getInstance(Hosts.HOST_URL);
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
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            localBuilder.addInterceptor(loggingInterceptor);
        }

        /**
         * 在建立retrofit实例前，调用设置证书的方法即可
         */
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
            localBuilder.readTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
            localBuilder.writeTimeout(RW_TIMEOUT, TimeUnit.MINUTES);
            //错误重连
            localBuilder.retryOnConnectionFailure(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(localBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                    .build();
            requestAPI = retrofit.create(RequestAPI.class);
            requestApiCache.put(baseUrl, requestAPI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkInit() {
        if (requestAPI == null) {
            throw new RuntimeException("RequestHelper must be init,Please call init method.");
        }
    }


    public RxManager get(String url, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executeGetCall(url)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new MyError())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new RxManager(mCompositeSubscription);
    }

    public RxManager get(String url, HashMap<String, Object> params, HTCallBack cb) {
        checkInit();
        RequestBody body = toRequestBody(params);
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executeGetCall(url, body)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new MyError())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new RxManager(mCompositeSubscription);
    }

    public RxManager post(String url, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.executePostCall(url)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new MyError())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new RxManager(mCompositeSubscription);
    }

    public RxManager post(String url, HashMap<String, Object> params, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        RequestBody body = toRequestBody(params);
        Subscription subscription = requestAPI.executePostCall(url, body)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new MyError())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new RxManager(mCompositeSubscription);
    }

    public RxManager postImg(String url, File file, HTCallBack cb) {
        checkInit();
        CompositeSubscription mCompositeSubscription = new CompositeSubscription();
        Subscription subscription = requestAPI.postImg(url, toDesRequestBody("head"), toMultipartBody(file))
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new MyError())
                .subscribe(new MyAction(cb, mCompositeSubscription));
        mCompositeSubscription.add(subscription);
        return new RxManager(mCompositeSubscription);
    }


    class MyError implements Func1<Throwable, Response<ResponseBody>> {

        @Override
        public Response<ResponseBody> call(Throwable throwable) {
            LogUtils.e(throwable);
            if (throwable == null) {
                return null;
            }
            ResponseBody requestBody = ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), throwable.getMessage());
            return Response.error(ApiErrorCode.ERROR_UNKNOWO.getCode(), requestBody);
        }
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
            try {
                LogUtils.i(responseBodyResponse);
                boolean intercept = false;
                if (interception != null) {
                    intercept = interception.intercept(responseBodyResponse);
                }
                if (!intercept) {
                    if (responseBodyResponse.isSuccessful()) {
                        if (cb != null) {
                            try {
                                String result = responseBodyResponse.body().string();
                                LogUtils.i(result);
                                Type t = ((ParameterizedType) cb.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                                HttpResponse response = new Gson().fromJson(result, t);
                                if (response.isResult() || response.getCode() == 200 || response.getErrorCode() >= 0) {
                                    cb.onSuccess(response);
                                } else {
                                    cb.onError(new ApiException(response.getCode(), response.getMsg()));
                                }
                            } catch (Exception e) {
                                LogUtils.e(e);
                                try {
                                    cb.onError(new ApiException(responseBodyResponse.code(), e.getMessage()));
                                } catch (Exception e1) {
                                    LogUtils.e(e1);
                                }
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
                            try {
                                cb.onError(new ApiException(code, msg));
                            } catch (Exception e1) {
                                LogUtils.e(e1);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtils.e(e);
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

    private RequestBody toDesRequestBody(String des) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), des);
    }

    private MultipartBody.Part toMultipartBody(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        return MultipartBody.Part.createFormData("file", file.getName().trim(), requestFile);
    }

    public void setInterception(Interception interception) {
        this.interception = interception;
    }

    public interface Interception {
        public boolean intercept(Response<ResponseBody> responseBodyResponse);
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
}

