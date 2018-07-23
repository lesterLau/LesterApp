package com.lesterlau.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lesterlau.base.R;
import com.lesterlau.base.ui.TitlePanel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liubin on 2017/10/10.
 */
public abstract class BaseSimpleFragment extends Fragment {
    /**
     * 判断是否是第一次加载，当数据加载完成时，置为false
     */
    protected boolean isFirst = true;
    /**
     * 判断当前fragment 是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 界面是否初始化完毕
     */
    protected boolean isPrepare;
    /**
     *
     */
    protected Unbinder unbinder;
    /**
     * contentView 方便统一处理view
     */
    protected ViewGroup contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = (ViewGroup) inflater.inflate(getContentLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateInit(savedInstanceState);
        initView();
        isFirst = true;
    }

    /**
     * 数据懒加载判断
     */
    public void lazzLodeData() {
        //隐藏状态下或者懒加载非第一次的情况下 不加载数据
        if (!isPrepare || !isVisibleToUser || isLazzLoad() && !isFirst) {
            return;
        }
        initData();
        isFirst = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPrepare = true;
        lazzLodeData();
    }


    @Override
    public void onPause() {
        super.onPause();
        isPrepare = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                // 将所有正等待显示的子Fragment设置为显示状态，并取消等待显示标记
                List<Fragment> childFragmentList = getChildFragmentManager().getFragments();
                if (childFragmentList != null && childFragmentList.size() > 0) {
                    for (Fragment childFragment : childFragmentList) {
                        if (childFragment.getUserVisibleHint()) {
                            childFragment.setUserVisibleHint(isVisibleToUser);
                        }
                    }
                }
            }
        }
        this.isVisibleToUser = isVisibleToUser;
        lazzLodeData();
    }

    /**
     * onCreateInit  Bundle处理
     *
     * @param savedInstanceState
     */
    protected void onCreateInit(Bundle savedInstanceState) {
    }

    /**
     * 布局id
     * 需要子类重写
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 是否懒加载
     *
     * @return
     */
    protected boolean isLazzLoad() {
        return true;
    }
}
