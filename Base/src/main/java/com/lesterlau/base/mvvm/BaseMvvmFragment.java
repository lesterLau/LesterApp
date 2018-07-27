package com.lesterlau.base.mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;
import com.lesterlau.base.ui.fragment.BaseSimpleFragment;

import butterknife.ButterKnife;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public abstract class BaseMvvmFragment<VM extends BaseViewModel> extends BaseSimpleFragment {
    protected ViewDataBinding mViewDataBinding;
    protected VM videModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getContentLayoutId(), container, false);
        videModel = initViewModel();
        contentView = (ViewGroup) mViewDataBinding.getRoot();
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }
    protected abstract VM initViewModel();
}
