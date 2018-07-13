package com.lester.lesterapp.test.fragment;

import android.widget.TextView;

import com.lester.lesterapp.R;
import com.lesterlau.base.BaseFragment;

import butterknife.BindView;

public class LazzFragmentTest extends BaseFragment {
    @BindView(R.id.tv_test)
    TextView tvTest;

    private int count;

    @Override
    protected int getContentLayoutId() {
        return R.layout.content_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        count++;
        tvTest.setText("第" + count + "次加载数据");
    }

    @Override
    protected boolean isLazzLoad() {
        return true;
    }
}
