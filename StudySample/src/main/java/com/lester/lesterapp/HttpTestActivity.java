package com.lester.lesterapp;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.lesterlau.base.BaseActivity;
import com.lesterlau.http.ApiException;
import com.lesterlau.http.HTCallBack;
import com.lesterlau.http.HttpResponse;
import com.lesterlau.http.RequestHelper;
import com.lesterlau.http.RxManager;

import butterknife.BindView;
import butterknife.OnClick;

public class HttpTestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    TextView tvShow;
    private RxManager rxManager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_http_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_get, R.id.tv_get_with_params, R.id.tv_post, R.id.tv_post_with_params})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get:
                RequestHelper.getInstance().get("banner/json", new HTCallBack<HttpResponse<String, String>>() {

                    @Override
                    public void onSuccess(HttpResponse<String, String> response) {
                        LogUtils.e(response);
                        tvShow.setText(response.getData());
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                });
                break;
            case R.id.tv_get_with_params:
                break;
            case R.id.tv_post:
                break;
            case R.id.tv_post_with_params:
                break;
        }
    }
}
