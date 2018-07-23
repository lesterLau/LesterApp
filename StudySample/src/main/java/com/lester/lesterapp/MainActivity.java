package com.lester.lesterapp;

import android.graphics.Color;
import android.os.Environment;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.lester.lesterapp.test.FragmentTestActivity;
import com.lester.lesterapp.test.HttpTestActivity;
import com.lesterlau.base.ui.NoDoubleClickListener;
import com.lesterlau.base.ui.activity.BaseActivity;
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
        NoDoubleClickListener listener = new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showNormal();
            }
        };
//        errorPanel.setErrorIcon(R.mipmap.ic_launcher);
//        errorPanel.setErrorTips("数据加载失败了~！");
//        errorPanel.setErrorTips("数据加载失败了~！", Color.RED);
//        errorPanel.setErrorTips(R.string.app_name);
//        errorPanel.setErrorTips(R.string.app_name, Color.RED);
//        errorPanel.setErrorListener(listener);
//        errorPanel.setErrorListener("刷新");
//        errorPanel.setErrorListener("刷新",Color.RED);
//        errorPanel.setErrorListener("刷新",Color.RED,listener);
        errorPanel.setErrorListener(R.string.app_name);
//        errorPanel.setErrorListener("刷新",Color.RED);
//        errorPanel.setErrorListener("刷新",Color.RED,listener);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_http, R.id.tv_tinker, R.id.tv_fragment, R.id.tv_news, R.id.tv_show_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_http:
                ActivityUtils.startActivity(HttpTestActivity.class);
                break;
            case R.id.tv_tinker:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                break;
            case R.id.tv_fragment:
                ActivityUtils.startActivity(FragmentTestActivity.class);
                break;
            case R.id.tv_news:
                ARouter.getInstance().build("/news/index").navigation();
                break;
            case R.id.tv_show_error:
                showError();
                break;
        }
    }

}
