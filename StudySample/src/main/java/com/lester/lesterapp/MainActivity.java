package com.lester.lesterapp;

import android.Manifest;
import android.os.Environment;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lesterlau.base.BaseActivity;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import butterknife.OnClick;


public class MainActivity extends BaseActivity {


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

    @OnClick({R.id.tv_http, R.id.tv_tinker, R.id.tv_fragment, R.id.tv_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_http:
                ActivityUtils.startActivity(HttpTestActivity.class);
                break;
            case R.id.tv_tinker:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                break;
            case R.id.tv_fragment:
                break;
            case R.id.tv_news:
                break;
        }
    }

}
