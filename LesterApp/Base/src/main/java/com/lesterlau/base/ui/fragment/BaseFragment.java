package com.lesterlau.base.ui.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lesterlau.base.R;
import com.lesterlau.base.keeplive.NetworkReceiver;
import com.lesterlau.base.ui.ErrorPanel;
import com.lesterlau.base.ui.TitlePanel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liubin on 2017/10/10.
 */
public abstract class BaseFragment extends BaseSimpleFragment implements NetworkReceiver.CallBack {

    /**
     * 标题栏处理
     */
    protected TitlePanel titlePanel;
    /**
     * 数据加载出错统一处理
     */
    protected ErrorPanel errorPanel;

    protected View realContentView;

    protected NetworkReceiver networkReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(R.layout.base_content_layout, null);
        titlePanel = new TitlePanel(getContext(), contentView);
        inflater.inflate(getContentLayoutId(), contentView, true);
        realContentView = contentView.getChildAt(1);
        errorPanel = new ErrorPanel(getContext(), contentView);
        if (!isAttachTitle()) {
            titlePanel.setVisibility(View.GONE);
        }
        if (isListenerNetwork()) {
            registerNetworkReceiver();
        }
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

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

    protected void showError() {
        errorPanel.setVisibility(View.VISIBLE);
        realContentView.setVisibility(View.GONE);
    }

    protected void showNormal() {
        errorPanel.setVisibility(View.GONE);
        realContentView.setVisibility(View.VISIBLE);
    }

    protected void registerNetworkReceiver() {
        networkReceiver = new NetworkReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getContext().registerReceiver(networkReceiver, intentFilter);
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
    public void onDestroy() {
        super.onDestroy();
        if (networkReceiver != null) {
            getContext().unregisterReceiver(networkReceiver);
        }
    }
}
