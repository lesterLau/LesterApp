package com.lester.lesterapp.test;

import android.widget.TextView;

import com.lester.lesterapp.R;
import com.lester.lesterapp.test.contract.TestContract;
import com.lester.lesterapp.test.presenter.TestPresenter;
import com.lesterlau.base.mvp.BaseMvpActivity;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public class MvpTestActivity extends BaseMvpActivity<TestPresenter> implements TestContract.View {
    @BindView(R.id.tv_show)
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
    public void showTest(CharSequence charSequence) {
        tvShow.setText(charSequence);
    }

    @OnClick(R.id.tv_show)
    public void onViewClicked() {
        mPresenter.setTestStr(new Random().nextInt(1000) + "---->");
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
