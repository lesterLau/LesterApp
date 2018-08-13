package com.lester.jni;


import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lesterlau.base.ui.activity.BaseActivity;


/**
 * @Author lester
 * @Date 2018/8/1
 */
@Route(path = "/jni/index")
public class JniMainActivity extends BaseActivity {
    TextView tvShow;

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_jni_main;
    }

    @Override
    protected void initView() {
        tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setText(NdkTools.getStringFromNDK());
    }

    @Override
    protected void initData() {

    }
}
