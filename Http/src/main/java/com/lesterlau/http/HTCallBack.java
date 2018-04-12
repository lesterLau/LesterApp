package com.lesterlau.http;

/**
 * Created by  lesterlau on 2018/4/9.
 */

public abstract class HTCallBack {
    public abstract void onSuccess(HttpResponse httpResponse);

    public abstract void onError(ApiException e);
}
