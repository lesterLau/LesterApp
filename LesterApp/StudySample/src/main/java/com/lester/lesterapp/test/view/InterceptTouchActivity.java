package com.lester.lesterapp.test.view;

import android.os.Bundle;
import android.view.MotionEvent;

import com.blankj.utilcode.util.LogUtils;
import com.lester.lesterapp.R;
import com.lesterlau.base.ui.activity.BaseActivity;
import com.lesterlau.view.intercept.InterceptTouchLinearLayout;
import com.lesterlau.view.intercept.InterceptTouchRelativeLayout;
import com.lesterlau.view.intercept.OnInterceptTouchEventLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class InterceptTouchActivity extends BaseActivity {
    @BindView(R.id.ll)
    InterceptTouchLinearLayout ll;
    @BindView(R.id.rl)
    InterceptTouchRelativeLayout rl;

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
        return R.layout.activity_intercept_touch_test;
    }

    @Override
    protected void initView() {
        ll.setInterceptTouchEvent(true);
        ll.setOnInterceptTouchEvent(new OnInterceptTouchEventLayout.OnInterceptTouchEvent() {
            @Override
            public void interceptTouchEvent(MotionEvent ev) {
                LogUtils.e("ll---------------------->");
                LogUtils.e(ev);
            }
        });
        rl.setInterceptTouchEvent(true);
        rl.setOnInterceptTouchEvent(new OnInterceptTouchEventLayout.OnInterceptTouchEvent() {
            @Override
            public void interceptTouchEvent(MotionEvent ev) {
                LogUtils.e("rl---------------------->");
                LogUtils.e(ev);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
