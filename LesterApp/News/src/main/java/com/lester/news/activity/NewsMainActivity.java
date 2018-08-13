package com.lester.news.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lester.news.R;
import com.lesterlau.base.ui.activity.BaseActivity;

/**
 * Created by Lester on 2017/2/26.
 */
@Route(path = "/news/index")
public class NewsMainActivity extends BaseActivity {

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.news_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
