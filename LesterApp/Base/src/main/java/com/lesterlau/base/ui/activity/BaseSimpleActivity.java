package com.lesterlau.base.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.blankj.utilcode.util.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by liubin on 2017/10/10.
 */
public abstract class BaseSimpleActivity extends SwipeBackActivity {
    private static final String TAG = BaseSimpleActivity.class.getSimpleName();
    protected Activity instance;
    private Unbinder unbinder;
    protected int defaultPageIndex = 1;
    protected int pageSize = 10;
    protected int page = defaultPageIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        instance = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, "onCreate");
        onCreateInit(savedInstanceState);
        initBase(getContentLayoutId());
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    protected void initBase(int layoutResID) {
        setContentView(layoutResID);
    }

    /**
     * onCreateInit  Bundle处理
     *
     * @param savedInstanceState
     */
    protected void onCreateInit(Bundle savedInstanceState) {
    }

    /**
     * 布局id
     * 需要子类重写
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
