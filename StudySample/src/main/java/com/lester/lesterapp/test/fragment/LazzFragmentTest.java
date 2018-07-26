package com.lester.lesterapp.test.fragment;

import android.view.View;
import android.widget.TextView;

import com.lester.lesterapp.R;
import com.lester.lesterapp.observe.MsgMgr;
import com.lesterlau.base.ui.NoDoubleClickListener;
import com.lesterlau.base.ui.fragment.BaseFragment;

import butterknife.BindView;

public class LazzFragmentTest extends BaseFragment {
    @BindView(R.id.tv_test)
    TextView tvTest;

    private int count;

    @Override
    protected int getContentLayoutId() {
        return R.layout.content_test;
    }

    @Override
    protected void initView() {
        tvTest.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                MsgMgr.getInstance().sendMsg("key", "value----->第" + count + "次加载数据");
            }
        });
    }

    @Override
    protected void initData() {
        count++;
        tvTest.setText("第" + count + "次加载数据");
    }

    @Override
    protected boolean isLazzLoad() {
        return true;
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
