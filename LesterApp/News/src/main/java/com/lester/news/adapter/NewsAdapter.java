package com.lester.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lester.news.R;
import com.lester.news.bean.NewsBean;

import java.util.List;

/**
 * Created by Lester on 2017/2/26.
 */

public class NewsAdapter extends ArrayAdapter {

    private int resId;

    public NewsAdapter(Context context, int resource, List<NewsBean> objects) {
        super(context, resource, objects);
        resId = resource;
    }

    @Nullable
    @Override
    public NewsBean getItem(int position) {
        return (NewsBean) super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resId, null);
        }
        TextView textView = convertView.findViewById(R.id.news_title);
        textView.setText(getItem(position).title);
        return convertView;
    }
}
