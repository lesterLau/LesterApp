package com.lester.mvvm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lester.mvvm.R;
import com.lester.mvvm.fragment.MvvmFragmentTest;
import com.lesterlau.base.ui.activity.BaseActivity;
import com.lesterlau.view.AbViewPager;

import java.util.ArrayList;
import java.util.List;

public class MvvmFragmentActivity extends BaseActivity {
    AbViewPager pager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm_fragment_test;
    }

    @Override
    protected void initView() {
        pager = (AbViewPager) findViewById(R.id.abpager);
        MvvmFragmentTest mvvmFragmentTest1 = new MvvmFragmentTest();
        Bundle args1 = new Bundle();
        args1.putString("tag", "mvvmFragmentTest1");
        mvvmFragmentTest1.setArguments(args1);
        MvvmFragmentTest mvvmFragmentTest2 = new MvvmFragmentTest();
        Bundle args2 = new Bundle();
        args2.putString("tag", "mvvmFragmentTest2");
        mvvmFragmentTest2.setArguments(args2);
        fragmentList.add(mvvmFragmentTest2);
        fragmentList.add(mvvmFragmentTest1);
        pager.setPagingEnabled(false);
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
