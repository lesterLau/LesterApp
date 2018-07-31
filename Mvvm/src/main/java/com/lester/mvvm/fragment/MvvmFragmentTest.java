package com.lester.mvvm.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.lester.mvvm.BR;
import com.lester.mvvm.R;
import com.lester.mvvm.viewmodel.TestViewModel;
import com.lesterlau.base.mvvm.BaseMvvmFragment;
import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public class MvvmFragmentTest extends BaseMvvmFragment<TestViewModel> {
    private String tag;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm_test;
    }

    @Override
    protected void onCreateInit(Bundle savedInstanceState) {
        super.onCreateInit(savedInstanceState);
        tag = getArguments().getString("tag");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected boolean isLazzLoad() {
        return false;
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(tag)) {
            videModel.setUsername(tag);
        }
    }

    @Override
    protected TestViewModel initViewModel() {
        TestViewModel testViewModel = new TestViewModel(getActivity());

        mViewDataBinding.setVariable(BR.viewmodel, testViewModel);
        return testViewModel;
    }
}
