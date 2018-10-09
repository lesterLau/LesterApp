package com.lesterlau.base.mvp.view;

import android.app.Activity;

/**
 * @Author lester
 * @Date 2018/7/24
 */
public interface IBaseView {
    /**
     * showNormal
     */
    void showNormal();

    /**
     * Show error
     */
    void showError();

    /**
     * getInstance
     */
    Activity getInstance();
}
