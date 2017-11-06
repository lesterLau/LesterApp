package com.lester.lesterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lester.news.activity.NewsMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsMainActivity.actionStart(MainActivity.this);
            }
        });
    }
}
