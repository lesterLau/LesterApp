package com.lesterlau.base.ui;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lesterlau.base.R;


/**
 * Created by liubin on 2018/1/5.
 */

public class TitlePanel extends BasePanel {

    public TitlePanel(Activity context, LinearLayout container) {
        super(context);
        setContentView(R.layout.base_title, container, true);
    }

    /**
     * 自定义标题栏左侧view
     *
     * @param view
     */
    public void setLeftView(View view, NoDoubleClickListener listener) {
        LinearLayout container = findViewById(R.id.ll_left_container);
        container.addView(view);
        container.setOnClickListener(listener);
        container.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义标题栏左侧view
     *
     * @param resId
     */
    public void setLeftView(@LayoutRes int resId, NoDoubleClickListener listener) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        setLeftView(view, listener);
    }

    /**
     * 自定义标题栏左侧view
     *
     * @param view
     */
    public void setLeftView(View view) {
        setLeftView(view, null);
    }

    /**
     * 自定义标题栏左侧view
     *
     * @param resId
     */
    public void setLeftView(@LayoutRes int resId) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        setLeftView(view, null);
    }

    /**
     * 设置默认标题栏左侧图片和文字
     *
     * @param resId
     * @param str
     * @param listener
     */
    public void setLeftView(int resId, String str, NoDoubleClickListener listener) {
        LinearLayout container = findViewById(R.id.ll_left);
        ImageView imageView = findViewById(R.id.image_left);
        TextView textView = findViewById(R.id.tv_left);
        if (resId > 0) {
            imageView.setImageResource(resId);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(str)) {
            textView.setText(str);
        } else {
            textView.setVisibility(View.GONE);
        }
        container.setOnClickListener(listener);
        container.setVisibility(View.VISIBLE);
    }

    /**
     * 设置默认标题栏左侧图片和文字
     *
     * @param resId
     * @param strId
     * @param listener
     */
    public void setLeftView(int resId, int strId, NoDoubleClickListener listener) {
        setLeftView(resId, context.getString(strId), listener);
    }

    /**
     * 设置默认标题栏左侧图片和文字
     *
     * @param resId
     * @param str
     */
    public void setLeftView(int resId, String str) {
        setLeftView(resId, str, null);
    }

    /**
     * 设置默认标题栏左侧图片和文字
     *
     * @param resId
     * @param strId
     */
    public void setLeftView(int resId, int strId) {
        setLeftView(resId, context.getString(strId), null);
    }

    /**
     * 设置默认标题栏左侧文字
     *
     * @param str
     * @param listener
     */
    public void setLeftText(String str, NoDoubleClickListener listener) {
        setLeftView(-1, str, listener);
    }

    /**
     * 设置默认标题栏左侧文字
     *
     * @param
     * @param listener
     */
    public void setLeftText(int resId, NoDoubleClickListener listener) {
        setLeftView(-1, context.getString(resId), listener);
    }

    /**
     * 设置默认标题栏左侧文字
     *
     * @param str
     */
    public void setLeftText(String str) {
        setLeftView(-1, str, null);
    }

    /**
     * 设置默认标题栏左侧文字
     *
     * @param resId
     */
    public void setLeftText(int resId) {
        setLeftView(-1, context.getString(resId), null);
    }

    /**
     * 设置默认标题栏左侧图片
     *
     * @param resId
     * @param listener
     */
    public void setLeftImage( int resId, NoDoubleClickListener listener) {
        setLeftView(resId, null, listener);
    }

    /**
     * 设置默认标题栏左侧图片
     *
     * @param resId
     */
    public void setLeftImage( int resId) {
        setLeftView(resId, null, null);
    }

    /**
     * 设置默认标题栏左侧返回
     *
     * @param resId
     */
    public void setBackView(int resId, String str) {
        setLeftView(resId, str, new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                back();
            }
        });
    }

    /**
     * 设置默认标题栏左侧返回
     *
     * @param resId
     */
    public void setBackView(int resId) {
        setBackView(resId, null);
    }

    /**
     * 设置默认标题栏左侧返回
     *
     * @param str
     */
    public void setBackView(String str) {
        setBackView(R.drawable.back_icon, str);
    }

    /**
     * 设置默认标题栏左侧返回
     */
    public void setBackView() {
        setBackView(R.drawable.back_icon, null);
    }

    /**
     * 自定义标题栏中心view
     *
     * @param view
     */
    public void setCenterView(View view, NoDoubleClickListener listener) {
        LinearLayout container = findViewById(R.id.ll_center_container);
        container.addView(view);
        container.setOnClickListener(listener);
        container.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义标题栏中心view
     *
     * @param view
     */
    public void setCenterView(View view) {
        setCenterView(view, null);
    }

    /**
     * 自定义标题栏中心view
     *
     * @param resId
     */
    public void setCenterView(@LayoutRes int resId, NoDoubleClickListener listener) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        setCenterView(view, listener);
    }

    /**
     * 自定义标题栏中心view
     *
     * @param resId
     */
    public void setCenterView(@LayoutRes int resId) {
        setCenterView(resId, null);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title, @ColorInt int titleColor) {
        TextView tvTitle = findViewById(R.id.tv_center);
        tvTitle.setText(title);
        tvTitle.setTextColor(titleColor);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        TextView tvTitle = findViewById(R.id.tv_center);
        tvTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param resId
     */
    public void setTitle(int resId) {
        TextView tvTitle = findViewById(R.id.tv_center);
        tvTitle.setText(resId);
    }

    /**
     * 自定义标题栏右侧view
     *
     * @param view
     */
    public void setRightView(View view, NoDoubleClickListener listener) {
        LinearLayout container = findViewById(R.id.ll_right_container);
        container.addView(view);
        container.setOnClickListener(listener);
        container.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义标题栏右侧view
     *
     * @param view
     */
    public void setRightView(View view) {
        setRightView(view, null);
    }

    /**
     * 自定义标题栏右侧view
     *
     * @param resId
     */
    public void setRightView(@LayoutRes int resId, NoDoubleClickListener listener) {
        View view = LayoutInflater.from(context).inflate(resId, null);
        setRightView(view, listener);
    }

    /**
     * 自定义标题栏右侧view
     *
     * @param resId
     */
    public void setRightView(@LayoutRes int resId) {
        setRightView(resId, (NoDoubleClickListener) null);
    }

    /**
     * 设置默认标题栏右侧图片和文字
     *
     * @param resId
     * @param str
     * @param listener
     */
    public void setRightView(int resId, String str, NoDoubleClickListener listener) {
        LinearLayout container = findViewById(R.id.ll_right);
        ImageView imageView = findViewById(R.id.image_right);
        TextView textView = findViewById(R.id.tv_right);
        if (resId > 0) {
            imageView.setImageResource(resId);
        } else {
            imageView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(str)) {
            textView.setText(str);
        } else {
            textView.setVisibility(View.GONE);
        }
        container.setOnClickListener(listener);
        container.setVisibility(View.VISIBLE);
    }

    /**
     * 设置默认标题栏右侧图片和文字
     *
     * @param resId
     * @param str
     */
    public void setRightView(int resId, String str) {
        setRightView(resId, str, null);
    }

    /**
     * 设置默认标题栏右侧文字
     *
     * @param str
     * @param listener
     */
    public void setRightText(String str, NoDoubleClickListener listener) {
        setRightView(-1, str, listener);
    }

    /**
     * 设置默认标题栏右侧文字
     *
     * @param str
     */
    public void setRightText(String str) {
        setRightView(-1, str, null);
    }

    /**
     * 设置默认标题栏右侧文字
     *
     * @param strId
     * @param listener
     */
    public void setRightText(int strId, NoDoubleClickListener listener) {
        setRightView(-1, context.getString(strId), listener);
    }

    /**
     * 设置默认标题栏右侧文字
     *
     * @param strId
     */
    public void setRightText(int strId) {
        setRightView(-1, context.getString(strId), null);
    }

    /**
     * 设置默认标题栏左侧图片
     *
     * @param resId
     * @param listener
     */
    public void setRightImage(int resId, NoDoubleClickListener listener) {
        setRightView(resId, null, listener);
    }

    /**
     * 设置默认标题栏左侧图片
     *
     * @param resId
     */
    public void setRightImage(int resId) {
        setRightView(resId, null, null);
    }

    /**
     * 返回
     */
    public void back() {
        //关闭键盘并finish当前页面
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
