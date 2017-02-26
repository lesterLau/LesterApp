package com.lester.news.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新闻实体类
 * Created by Lester on 2017/2/26.
 */

public class NewsBean implements Parcelable {

    public String title;

    public String content;

    public NewsBean() {
    }

    public NewsBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    protected NewsBean(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel in) {
            return new NewsBean(in);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}
