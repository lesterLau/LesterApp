package com.lesterlau.base.ui;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesterlau.base.R;

/**
 * Created by liubin on 2017/10/10.
 */
public class ErrorPanel extends BasePanel {
    public ErrorPanel(Context context, ViewGroup parentView) {
        super(context);
        setContentView(R.layout.error_layout, parentView, true);
    }

    /**
     * 设置错误提示信息
     *
     * @param tips
     * @param color
     */
    public void setErrorTips(CharSequence tips, @ColorInt int color) {
        TextView tvErrorTips = findViewById(R.id.tv_error_tips);
        tvErrorTips.setText(tips);
        tvErrorTips.setTextColor(color);
    }

    /**
     * 设置错误提示信息
     *
     * @param tips
     * @param color
     */
    public void setErrorTips(@StringRes int tips, @ColorInt int color) {
        setErrorTips(context.getString(tips), color);
    }

    /**
     * 设置错误提示信息
     *
     * @param tips
     */
    public void setErrorTips(CharSequence tips) {
        setErrorTips(tips, context.getResources().getColor(R.color.errorTipsColor));
    }

    /**
     * 设置错误提示信息
     *
     * @param tips
     */
    public void setErrorTips(@StringRes int tips) {
        setErrorTips(tips, context.getResources().getColor(R.color.errorTipsColor));
    }


    /**
     * 设置错误图标
     *
     * @param resId
     */
    public void setErrorIcon(@DrawableRes int resId) {
        ImageView ivError = findViewById(R.id.iv_error);
        ivError.setImageResource(resId);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param color
     * @param listener
     */
    public void setErrorListener(CharSequence loadText, @ColorInt int color, NoDoubleClickListener listener) {
        Button btnReload = findViewById(R.id.btn_reload);
        btnReload.setText(loadText);
        btnReload.setTextColor(color);
        btnReload.setOnClickListener(listener);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param color
     * @param listener
     */
    public void setErrorListener(int loadText, @ColorInt int color, NoDoubleClickListener listener) {
        setErrorListener(context.getString(loadText), color, listener);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param listener
     */
    public void setErrorListener(CharSequence loadText, NoDoubleClickListener listener) {
        setErrorListener(loadText, context.getResources().getColor(R.color.errorReloadColor), listener);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param listener
     */
    public void setErrorListener(int loadText, NoDoubleClickListener listener) {
        setErrorListener(context.getString(loadText), context.getResources().getColor(R.color.errorReloadColor), listener);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param color
     */
    public void setErrorListener(CharSequence loadText, @ColorInt int color) {
        setErrorListener(loadText, color, null);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     * @param color
     */
    public void setErrorListener(int loadText, @ColorInt int color) {
        setErrorListener(context.getString(loadText), color, null);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     */
    public void setErrorListener(CharSequence loadText) {
        setErrorListener(loadText, context.getResources().getColor(R.color.errorReloadColor), null);
    }

    /**
     * 设置加载点击
     *
     * @param loadText
     */
    public void setErrorListener(int loadText) {
        setErrorListener(context.getString(loadText), context.getResources().getColor(R.color.errorReloadColor), null);
    }

    /**
     * 设置加载点击
     *
     * @param listener
     */
    public void setErrorListener(NoDoubleClickListener listener) {
        setErrorListener(context.getString(R.string.reload), context.getResources().getColor(R.color.errorReloadColor), listener);
    }

    @Override
    public void setVisibility(int visibility) {
        if (contentView != null && contentView.findViewById(R.id.ll_error_root) != null) {
            contentView.findViewById(R.id.ll_error_root).setVisibility(visibility);
        }
    }

}
