package com.lester.mvp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lester.mvp.R;
import com.lester.mvp.fragment.MvpFragmentTest;
import com.lesterlau.base.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MvpFragmentActivity extends BaseActivity {
    ViewPager pager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvp_fragment_test;
    }

    @Override
    protected void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        fragmentList.add(new MvpFragmentTest());
        pager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), fragmentList));
        pager.setOffscreenPageLimit(fragmentList.size());
    }

    @Override
    protected void initData() {

    }

    //适配器
    public class FragPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public FragPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
