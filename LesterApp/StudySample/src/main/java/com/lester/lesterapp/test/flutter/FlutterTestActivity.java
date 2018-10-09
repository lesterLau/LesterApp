package com.lester.lesterapp.test.flutter;

import android.support.v4.app.FragmentTransaction;

import com.lester.lesterapp.R;
import com.lesterlau.base.ui.activity.BaseActivity;

//import io.flutter.facade.Flutter;

public class FlutterTestActivity extends BaseActivity {

    @Override
    protected boolean isAttachTitle() {
        return false;
    }

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_flutter_test;
    }

    @Override
    protected void initView() {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace(R.id.root, Flutter.createFragment("route1"));
//        tx.commit();
    }

    @Override
    protected void initData() {

    }
}
