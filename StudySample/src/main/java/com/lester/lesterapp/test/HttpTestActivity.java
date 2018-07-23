package com.lester.lesterapp.test;

import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.lester.lesterapp.R;
import com.lester.lesterapp.test.bean.BannerBean;
import com.lesterlau.base.ui.activity.BaseActivity;
import com.lesterlau.http.ApiException;
import com.lesterlau.http.HTCallBack;
import com.lesterlau.http.HttpResponse;
import com.lesterlau.http.RequestHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HttpTestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    TextView tvShow;

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }

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
                RequestHelper.getInstance().get(new RequestHelper.HttpRequest("banner/json", new HTCallBack<HttpResponse<List<BannerBean>, String>>() {

                    @Override
                    public void onSuccess(HttpResponse<List<BannerBean>, String> response) {
                        LogUtils.d(response);
                        if (response.getData() == null) {
                            return;
                        }
                        StringBuffer sb = new StringBuffer();
                        sb.append("Resulut=");
                        for (int i = 0; i < response.getData().size(); i++) {
                            sb.append(response.getData().get(i).toString() + "\n");
                        }
                        tvShow.setText(sb.toString());
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                }, this));
                break;
            case R.id.tv_get_with_params:
                RequestHelper.getInstance().get(new RequestHelper.HttpRequest("banner/json", new HTCallBack<HttpResponse<List<BannerBean>, String>>() {

                    @Override
                    public void onSuccess(HttpResponse<List<BannerBean>, String> response) {
                        LogUtils.d(response);
                        if (response.getData() == null) {
                            return;
                        }
                        StringBuffer sb = new StringBuffer();
                        sb.append("Resulut=");
                        for (int i = 0; i < response.getData().size(); i++) {
                            sb.append(response.getData().get(i).toString() + "\n");
                        }
                        tvShow.setText(sb.toString());
                    }

                    @Override
                    public void onError(ApiException e) {

                    }
                }));
                break;
            case R.id.tv_post:
                break;
            case R.id.tv_post_with_params:
                break;
        }
    }
}
