package com.lester.mvp.contract;

import com.lesterlau.base.mvp.presenter.IBasePresenter;
import com.lesterlau.base.mvp.view.IBaseView;

/**
 * @Author lester
 * @Date 2018/7/26
 */
public interface TestContract {

    interface View extends IBaseView {
        void showTest(CharSequence charSequence);
    }

    interface Presenter extends IBasePresenter<View> {
        void setTestStr(CharSequence charSequence);
    }

}
