package com.lester.lesterapp.test.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lester.lesterapp.R;
import com.lesterlau.base.ui.activity.BaseActivity;
import com.lesterlau.view.flow.FlowLayout;
import com.lesterlau.view.flow.TagAdapter;
import com.lesterlau.view.flow.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class FlowActivity extends BaseActivity {
    @BindView(R.id.tagFlowLayout)
    TagFlowLayout tagFlowLayout;
    private List<String> data = new ArrayList<>();
    private TagAdapter<String> mAdapter;

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_flow_test;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        String[] s = new String[]{"a", "b", "c", "d", "e", "1", "2", "3", "4", "5"};
        for (int i = 0; i < 10; i++) {
            sb.delete(0, sb.length());
            int count = random.nextInt(10);
            if (count <= 0) {
                count = 10;
            }
            for (int j = 0; j < count; j++) {
                sb.append(s[j]);
            }
            data.add(sb.toString());
        }
        mAdapter = new TagAdapter<String>(data) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(instance).inflate(R.layout.flow_test_tv, tagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(mAdapter);
    }
}
