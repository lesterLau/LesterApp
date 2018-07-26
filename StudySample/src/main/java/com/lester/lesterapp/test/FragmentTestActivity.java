package com.lester.lesterapp.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lester.lesterapp.R;
import com.lester.lesterapp.test.fragment.LazzFragmentTest;
import com.lester.lesterapp.test.fragment.NormalFragmentTest;
import com.lesterlau.base.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FragmentTestActivity extends BaseActivity {
    @BindView(R.id.pager)
    ViewPager pager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_fragment_test;
    }

    @Override
    protected void initView() {
        fragmentList.add(new LazzFragmentTest());
        fragmentList.add(new NormalFragmentTest());
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
