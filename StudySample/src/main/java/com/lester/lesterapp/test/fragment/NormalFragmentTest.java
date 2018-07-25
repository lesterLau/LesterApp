package com.lester.lesterapp.test.fragment;

import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lester.lesterapp.R;
import com.lester.lesterapp.observe.MsgMgr;
import com.lester.lesterapp.observe.PObserver;
import com.lesterlau.base.ui.fragment.BaseFragment;
import com.lesterlau.http.RxBus;

import butterknife.BindView;
import rx.functions.Action1;

public class NormalFragmentTest extends BaseFragment implements PObserver {
    @BindView(R.id.tv_test)
    TextView tvTest;

    private int count;

    @Override
    protected int getContentLayoutId() {
        return R.layout.content_test;
    }

    @Override
    protected void initView() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    protected void initData() {
        count++;
        tvTest.setText("第" + count + "次加载数据");
    }

    @Override
    protected boolean isLazzLoad() {
        return false;
    }

    @Override
    public void onMessage(String key, Object value) {
        ToastUtils.showShort("key=" + key + ",value=" + value);
    }
}
