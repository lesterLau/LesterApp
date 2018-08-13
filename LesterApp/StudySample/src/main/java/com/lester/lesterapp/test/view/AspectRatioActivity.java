package com.lester.lesterapp.test.view;

import com.lester.lesterapp.R;
import com.lesterlau.base.ui.activity.BaseActivity;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class AspectRatioActivity extends BaseActivity {
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
        return R.layout.activity_aspectratio_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}
