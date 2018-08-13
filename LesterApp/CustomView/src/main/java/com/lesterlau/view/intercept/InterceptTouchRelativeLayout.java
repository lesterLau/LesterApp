package com.lesterlau.view.intercept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 事件拦截的RelativeLayout
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class InterceptTouchRelativeLayout extends RelativeLayout implements OnInterceptTouchEventLayout {

    public InterceptTouchRelativeLayout(Context context) {
        super(context);
    }

    public InterceptTouchRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (onInterceptTouchEvent != null) {
            onInterceptTouchEvent.interceptTouchEvent(ev);
        }
        if (interceptTouchEvent) return true;
        return super.onInterceptTouchEvent(ev);
    }

    private boolean interceptTouchEvent = false;
    private OnInterceptTouchEvent onInterceptTouchEvent = null;

    /**
     * 拦截Touch事件，只是触发，不会真正拦截掉此事件
     *
     * @param onInterceptTouchEvent 点击事件
     */
    @Override
    public void setOnInterceptTouchEvent(OnInterceptTouchEvent onInterceptTouchEvent) {
        this.onInterceptTouchEvent = onInterceptTouchEvent;
    }

    /**
     * 设置是否强制拦截所有事件
     */
    public void setInterceptTouchEvent(boolean interceptTouchEvent) {
        this.interceptTouchEvent = interceptTouchEvent;
    }

}
