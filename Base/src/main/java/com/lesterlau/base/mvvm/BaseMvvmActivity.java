package com.lesterlau.base.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;
import com.lesterlau.base.ui.activity.BaseSimpleActivity;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public abstract class BaseMvvmActivity<VM extends BaseViewModel> extends BaseSimpleActivity {
    protected ViewDataBinding mViewDataBinding;
    protected VM videModel;

    @Override
    protected void initBase(int layoutResID) {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutResID);
        videModel = initViewModel();
    }

    protected abstract VM initViewModel();
}
