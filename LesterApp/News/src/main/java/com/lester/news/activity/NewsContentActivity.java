package com.lester.news.activity;

import android.content.Context;
import android.content.Intent;

import com.lester.news.R;
import com.lester.news.bean.NewsBean;
import com.lester.news.fragment.NewsContentFragment;
import com.lesterlau.base.ui.activity.BaseActivity;

/**
 * Created by Lester on 2017/2/26.
 */
public class NewsContentActivity extends BaseActivity {
    /**
     * @param context
     * @param newsBean
     */
    public static void actionStart(Context context, NewsBean newsBean) {
        Intent intent = new Intent();
        intent.setClass(context, NewsContentActivity.class);
        intent.putExtra("newsBean", newsBean);
        context.startActivity(intent);
    }

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    private Intent intent;

    private NewsBean newsBean;

    private NewsContentFragment contentFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.news_content;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        intent = getIntent();
        newsBean = intent.getParcelableExtra("newsBean");
        contentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        contentFragment.refresh(newsBean);
    }
}
