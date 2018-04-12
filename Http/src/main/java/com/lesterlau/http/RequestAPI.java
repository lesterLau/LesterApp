package com.lesterlau.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * retrofit请求接口封装
 * Created by  lesterlau on 2018/4/9.
 */
interface RequestAPI {

    // =============get请求============
    @GET
    Observable<Response<ResponseBody>> executeGetCall(@Url String url, @Body RequestBody body);

    @GET
    Observable<Response<ResponseBody>> executeGetCall(@Url String url);

    // =============post请求============
    @POST
    Observable<Response<ResponseBody>> executePostCall(@Url String url, @Body RequestBody body);

    @POST
    Observable<Response<ResponseBody>> executePostCall(@Url String url);
}