package com.lesterlau.http;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * retrofit请求接口封装
 * Created by liubin on 2016/9/19.
 */
interface RequestAPI {

    // =============get请求============
    @Headers({"User-Agent: "})
    @GET
    Observable<Response<ResponseBody>> executeGetCall(@Url String url, @Body RequestBody body);

    @Headers({"User-Agent: "})
    @GET
    Observable<Response<ResponseBody>> executeGetCall(@Url String url);

    // =============post请求============4
    @Headers({"User-Agent: "})
    @POST
    Observable<Response<ResponseBody>> executePostCall(@Url String url, @Body RequestBody body);

    @Headers({"User-Agent: "})
    @POST
    Observable<Response<ResponseBody>> executePostCall(@Url String url);

    @Headers({"User-Agent: "})
    @Multipart
    @POST
    Observable<Response<ResponseBody>>
    postImg(@Url String url, @Part("description") RequestBody description, @Part MultipartBody.Part file);

}