package com.lester.lesterapp;

import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lesterlau.base.BaseActivity;
import com.lesterlau.http.ApiException;
import com.lesterlau.http.HTCallBack;
import com.lesterlau.http.HttpResponse;
import com.lesterlau.http.RequestHelper;
import com.lesterlau.http.RxManager;

import java.util.List;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private RxManager rxManager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void finish() {
        super.finish();
        if (rxManager != null) {
            rxManager.cancel();
        }
    }

    @OnClick({R.id.tv_http, R.id.tv_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_http:
                break;
            case R.id.tv_news:
                break;
        }
    }

    private void testHttp() {
        rxManager = RequestHelper.getInstance().get("banner/json", new HTCallBack<HttpResponse<List<BannerBean>, List<BannerBean>>>() {

            @Override
            public void onSuccess(HttpResponse<List<BannerBean>, List<BannerBean>> bannerBeans) {
                LogUtils.e(bannerBeans);
                ToastUtils.showShort(bannerBeans.getData().size() + "");
            }

            @Override
            public void onError(ApiException e) {

            }
        });
    }

}
