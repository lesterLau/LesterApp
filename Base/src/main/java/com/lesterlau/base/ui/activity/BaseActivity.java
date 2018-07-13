package com.lesterlau.base.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.lesterlau.base.R;
import com.lesterlau.base.ui.TitlePanel;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by liubin on 2017/10/10.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Activity instance;
    /**
     * 标题栏处理
     */
    protected TitlePanel titlePanel;
    private Unbinder unbinder;

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
    }

    @Override
    public void finish() {
        LogUtils.d(TAG, "finish");
        unbinder.unbind();
        super.finish();
    }

    private void initBase(int layoutResID) {
        if (isAttachTitle()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View baseView = inflater.inflate(R.layout.base_content_layout, null);
            LinearLayout baseContent = baseView.findViewById(R.id.base_content);
            if (titlePanel == null) {
                titlePanel = new TitlePanel(this, baseContent);
            }
            inflater.inflate(layoutResID, baseContent, true);
            setContentView(baseView);
        } else {
            setContentView(getContentLayoutId());
        }
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

    /**
     * 是否添加公共title
     *
     * @return
     */
    protected boolean isAttachTitle() {
        return false;
    }
}
