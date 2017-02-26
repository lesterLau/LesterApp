package com.lester.news.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lester.news.R;
import com.lester.news.bean.NewsBean;

/**
 * 内容显示fragment
 * Created by Lester on 2017/2/26.
 */

public class NewsContentFragment extends Fragment {

    private View view;

    private LinearLayout visiblityLayout;

    private TextView titleTxt;

    private TextView contentTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_flag, container, false);
        visiblityLayout = (LinearLayout) view.findViewById(R.id.visiblity_layout);
        titleTxt = (TextView) view.findViewById(R.id.news_title);
        contentTxt = (TextView) view.findViewById(R.id.news_content);
        return view;
    }

    public void refresh(NewsBean newsBean) {
        titleTxt.setText(newsBean.title);
        contentTxt.setText(newsBean.content);
    }
}
