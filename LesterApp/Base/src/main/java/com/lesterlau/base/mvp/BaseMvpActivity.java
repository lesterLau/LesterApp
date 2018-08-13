package com.lesterlau.base.mvp;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lesterlau.base.R;
import com.lesterlau.base.keeplive.NetworkReceiver;
import com.lesterlau.base.mvp.presenter.IBasePresenter;
import com.lesterlau.base.mvp.view.IBaseView;
import com.lesterlau.base.ui.ErrorPanel;
import com.lesterlau.base.ui.TitlePanel;
import com.lesterlau.base.ui.activity.BaseSimpleActivity;

/**
 * @Author lester
 * @Date 2018/7/24
 */
public abstract class BaseMvpActivity<T extends IBasePresenter> extends BaseSimpleActivity implements IBaseView, NetworkReceiver.CallBack {
    protected T mPresenter;
    /**
     * 标题栏统一处理
     */
    protected TitlePanel titlePanel;
    /**
     * 数据加载出错统一处理
     */
    protected ErrorPanel errorPanel;

    protected View contentView;

    protected NetworkReceiver networkReceiver;

    @Override
    protected void onCreateInit(Bundle savedInstanceState) {
        super.onCreateInit(savedInstanceState);
        mPresenter = initPresenter();
    }

    @Override
    protected void initBase(int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup baseView = (ViewGroup) inflater.inflate(R.layout.base_content_layout, null);
        titlePanel = new TitlePanel(this, baseView);
        inflater.inflate(layoutResID, baseView, true);
        contentView = baseView.getChildAt(1);
        errorPanel = new ErrorPanel(this, baseView);
        setContentView(baseView);
        if (!isAttachTitle()) {
            titlePanel.setVisibility(View.GONE);
        }
        if (isListenerNetwork()) {
            registerNetworkReceiver();
        }
    }

    @Override
    protected void initView() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void initData() {
    }

    protected abstract T initPresenter();

    /**
     * 是否添加公共title
     *
     * @return
     */
    protected boolean isAttachTitle() {
        return false;
    }

    /**
     * 是否监听网络
     *
     * @return
     */
    protected boolean isListenerNetwork() {
        return false;
    }

    @Override
    public void showError() {
        errorPanel.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    @Override
    public void showNormal() {
        errorPanel.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }

    protected void registerNetworkReceiver() {
        networkReceiver = new NetworkReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    public void connectNetwork() {
        showNormal();
    }

    @Override
    public void connectWifi() {

    }

    @Override
    public void disConnectNetwork() {
        showError();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

}
