package com.lester.mvp.presenter;


import com.lester.mvp.contract.TestContract;
import com.lesterlau.base.mvp.presenter.BasePresenter;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public class TestPresenter extends BasePresenter<TestContract.View> implements TestContract.Presenter {
    @Override
    public void setTestStr(CharSequence charSequence) {
        mView.showTest(charSequence);
    }
}
