package com.lester.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.lester.news.R;
import com.lester.news.bean.NewsBean;
import com.lester.news.fragment.NewsContentFragment;

/**
 * Created by Lester on 2017/2/26.
 */
public class NewsContentActivity extends Activity {
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

    private Intent intent;

    private NewsBean newsBean;

    private NewsContentFragment contentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = getIntent();
        setContentView(R.layout.news_content);
        newsBean = intent.getParcelableExtra("newsBean");
        contentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
        contentFragment.refresh(newsBean);
    }
}
