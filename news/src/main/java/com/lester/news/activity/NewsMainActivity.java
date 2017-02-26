package com.lester.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.lester.news.R;

/**
 * Created by Lester on 2017/2/26.
 */
public class NewsMainActivity extends Activity {

    public static void actionStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NewsMainActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_main);
    }
}
