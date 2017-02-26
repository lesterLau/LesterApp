package com.lester.news.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lester.news.R;
import com.lester.news.activity.NewsContentActivity;
import com.lester.news.adapter.NewsAdapter;
import com.lester.news.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lester on 2017/2/26.
 */

public class NewsTitleFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Activity activity;

    private ListView newTitleListView;

    private List<NewsBean> newsBeanList;

    private NewsAdapter newsAdapter;

    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.news_title_frag, container, false);
        newsBeanList = getNewsBean();
        newsAdapter = new NewsAdapter(activity, R.layout.news_item, newsBeanList);
        newTitleListView = (ListView) view.findViewById(R.id.news_title_list_view);
        newTitleListView.setAdapter(newsAdapter);
        newTitleListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        if (activity.findViewById(R.id.news_content_layout) == null) {
            isTwoPane = false;
        } else {
            isTwoPane = true;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsBean newsBean = newsBeanList.get(position);
        if (isTwoPane) {
            NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
            newsContentFragment.refresh(newsBean);
        } else {
            NewsContentActivity.actionStart(activity, newsBean);
        }
    }

    private List<NewsBean> getNewsBean() {
        List<NewsBean> newsBeanList1 = new ArrayList<>();
        newsBeanList1.add(new NewsBean("Z11尝鲜：nubia UI迎来首个安卓7.1系统更新", "Nubia智能手机宣布为旗下旗舰手机Z11放出首个安卓7.1开发版更新，这也意味着努比亚成为国内少数适配安卓7.1的手机厂商之一。"));
        newsBeanList1.add(new NewsBean("之前说的索尼Xperia X2采用全面屏原来只是概念机", "这款“Xperia X2”渲染图原型是基于上一代Xperia XZ，你会惊奇地发现，一直被吐槽的长下巴没有了。"));
        newsBeanList1.add(new NewsBean("黑莓全键盘KEYone发布 骁龙625配安卓7.1", "TCL通讯科技控股有限公司在MWC前夕正式发布新款BlackBerry高端键盘智能手机——KEYone。"));
        newsBeanList1.add(new NewsBean("华为P10发布会今晚举行 新浪科技全程直播报道", "华为将于北京时间2月26日21点在巴塞罗那召开发布会，推出P系列新机HUAWEI P10，新浪科技将进行全方位直播报道。"));
        newsBeanList1.add(new NewsBean("LG G6曝光最新渲染图 短下巴+三种机身配色", "距离LG G6在MWC 2017上的发布只剩下不到两天了，就在产品正式发布前，外媒为我们带来了LG G6的多色渲染图。"));
        newsBeanList1.add(new NewsBean("新手教程：iOS设备取消App Store应用订阅", "如果你通过App Store订阅了一些服务，但是现在要取消订阅的话，只需要几个简单的步骤。"));
        newsBeanList1.add(new NewsBean("周末烩：小米闯巴基斯坦/OPPO销量依然坚挺", "MWC即将开始，我们回顾一下本周都发生了哪些行业热点事件，就当做是MWC大会开幕之前的一道开胃菜吧。"));
        newsBeanList1.add(new NewsBean("这里有复刻版诺基亚3310更多信息：支持更换面板", "诺基亚3310将运行Series 3+系统，采用彩色屏幕，整体给人的感觉就像是老款3310和诺基亚150的结合体。"));
        return newsBeanList1;
    }

}
