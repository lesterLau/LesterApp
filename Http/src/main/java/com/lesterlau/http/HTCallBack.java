package com.lesterlau.http;

/**
 * Created by liubin on 2018/4/23 0023.
 */
public abstract class HTCallBack<T> {
    public abstract void onSuccess(T t);

    public abstract void onError(ApiException e);
}
