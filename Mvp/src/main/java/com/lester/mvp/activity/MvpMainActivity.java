package com.lester.mvp.activity;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ActivityUtils;
import com.lester.mvp.R;
import com.lester.mvp.contract.TestContract;
import com.lester.mvp.presenter.TestPresenter;
import com.lesterlau.base.mvp.BaseMvpActivity;
import com.lesterlau.base.ui.NoDoubleClickListener;

import java.util.Random;

/**
 * @Author lester
 * @Date 2018/7/26
 */
@Route(path = "/mvp/index")
public class MvpMainActivity extends BaseMvpActivity<TestPresenter> implements TestContract.View {
    TextView tvShow;
    TextView tvToFragment;

    @Override
    protected TestPresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_mvp_test;
    }

    @Override
    protected void initView() {
        super.initView();
        tvShow = (TextView) findViewById(R.id.tv_show);
        tvToFragment = (TextView) findViewById(R.id.tv_to_fragment);
        tvToFragment.setText("to mvp fragment");
        tvShow.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mPresenter.setTestStr(new Random().nextInt(1000) + "---->");
            }
        });
        tvToFragment.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                ActivityUtils.startActivity(MvpFragmentActivity.class);
            }
        });
    }

    @Override
    public void showTest(CharSequence charSequence) {
        tvShow.setText(charSequence);
    }

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }
}
