package com.lester.lesterapp.test;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lester.lesterapp.R;
import com.lester.lesterapp.test.bean.ViewItemBean;
import com.lester.lesterapp.test.view.AspectRatioActivity;
import com.lester.lesterapp.test.view.CircleImageActivity;
import com.lester.lesterapp.test.view.FlowActivity;
import com.lester.lesterapp.test.view.InterceptTouchActivity;
import com.lesterlau.base.ui.NoDoubleClickListener;
import com.lesterlau.base.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class ViewTestActivity extends BaseActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private List<ViewItemBean> data = new ArrayList<>();

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
        return R.layout.activity_view_test;
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        data.add(new ViewItemBean("CircleImage", "CircleImageViewBase,ImageViewPlus extend CircleImageViewBase.", CircleImageActivity.class));
        data.add(new ViewItemBean("AspectRatio", "AspectRatioLinearLayout,AspectRatioRelativeLayout the width equal height.ChangeColorTextView textcolor is gradual change", AspectRatioActivity.class));
        data.add(new ViewItemBean("InterceptTouch", "InterceptTouchLinearLayout,InterceptTouchRelativeLayout can InterceptTouchEvent.ChangeColorTextView textcolor is gradual change", InterceptTouchActivity.class));
        data.add(new ViewItemBean("Flowlayout", "Flowlayout", FlowActivity.class));
        mRecyclerView.setAdapter(new ViewAdapter(android.R.layout.simple_list_item_2, data));
    }

    class ViewAdapter extends BaseQuickAdapter<ViewItemBean, BaseViewHolder> {
        public ViewAdapter(int layoutResId, @Nullable List<ViewItemBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final ViewItemBean item) {
            helper.setText(android.R.id.text1, item.getName());
            helper.setText(android.R.id.text2, item.getDesc());
            View root = (View) helper.getView(android.R.id.text1).getParent();
            root.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    ActivityUtils.startActivity(item.getCls());
                }
            });
        }
    }
}
