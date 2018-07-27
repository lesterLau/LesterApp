package com.lester.mvp.fragment;

import android.view.View;
import android.widget.TextView;

import com.lester.mvp.R;
import com.lester.mvp.contract.TestContract;
import com.lester.mvp.presenter.TestPresenter;
import com.lesterlau.base.mvp.BaseMvpFragment;
import com.lesterlau.base.ui.NoDoubleClickListener;

import java.util.Random;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public class MvpFragmentTest extends BaseMvpFragment<TestPresenter> implements TestContract.View {
    TextView tvShow;

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
        tvShow = contentView.findViewById(R.id.tv_show);
        tvShow.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mPresenter.setTestStr(new Random().nextInt(1000) + "---->");
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
