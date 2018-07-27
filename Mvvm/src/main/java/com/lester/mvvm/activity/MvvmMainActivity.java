package com.lester.mvvm.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ActivityUtils;
import com.lester.mvvm.BR;
import com.lester.mvvm.R;
import com.lester.mvvm.viewmodel.TestViewModel;
import com.lesterlau.base.mvvm.BaseMvvmActivity;
import com.lesterlau.base.mvvm.viewmodel.BaseViewModel;
import com.lesterlau.base.ui.NoDoubleClickListener;

/**
 * @Author lester
 * @Date 2018/7/26
 */
@Route(path = "/mvvm/index")
public class MvvmMainActivity extends BaseMvvmActivity {
    TextView tvToFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvvm_test;
    }

    @Override
    protected void initView() {
        tvToFragment = (TextView) findViewById(R.id.tv_to_fragment);
        tvToFragment.setText("to mvp fragment");
        tvToFragment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ActivityUtils.startActivity(MvvmFragmentActivity.class);
            }
        });
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
