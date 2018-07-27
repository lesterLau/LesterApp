package com.lester.lesterapp.test;

import com.lester.lesterapp.BR;
import com.lester.lesterapp.R;
import com.lester.lesterapp.test.viewmodel.TestViewModel;
import com.lesterlau.base.mvvm.BaseMvvmActivity;
import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public class MvvmTestActivity extends BaseMvvmActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm_test;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected BaseViewModel initViewModel() {
        TestViewModel testViewModel = new TestViewModel(this);
        mViewDataBinding.setVariable(BR.viewmodel, testViewModel);
        return testViewModel;
    }
}
