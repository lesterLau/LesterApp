package com.lesterlau.view.picker.common.popup;

import android.content.DialogInterface;
import android.support.annotation.CallSuper;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * 底部弹窗基类
 *
 * @Author lester
 * @Date 2018/7/30
 */
public abstract class BottomPopup<V extends View> implements DialogInterface.OnKeyListener {

    /* The constant MATCH_PARENT. */
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    /* The constant WRAP_CONTENT. */
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected FragmentActivity activity;
    protected int screenWidthPixels;
    protected int screenHeightPixels;
    private Popup popup;
    private int width = 0, height = -1;
    private boolean isFillScreen = false;
    private boolean isHalfScreen = false;

    /**
     * Instantiates a new Bottom popup.
     *
     * @param activity the activity
     */
    public BottomPopup(FragmentActivity activity) {
        this.activity = activity;
        screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        screenHeightPixels = activity.getResources().getDisplayMetrics().heightPixels;
        popup = new Popup(activity);
        popup.setOnKeyListener(this);
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    protected abstract V makeContentView();

    /**
     * 弹出窗显示之前调用，初始化窗体大小等
     */
    private void onShowPrepare() {
        setContentViewBefore();
        V view = makeContentView();
        if (popup != null) {
            // 设置弹出窗体的布局
            popup.setContentView(view);
        }
        setContentViewAfter(view);
        if (width == 0 && height == -1) {
            //未明确指定宽高，宽度优先考虑屏幕的4/5，高度优先考虑全屏再考虑半屏然后再考虑包裹内容
            width = (int) (screenWidthPixels * 0.8);
            if (isFillScreen) {
                height = MATCH_PARENT;
            } else if (isHalfScreen) {
                height = screenHeightPixels / 2;
            } else {
                height = WRAP_CONTENT;
            }
        }
        if (popup != null) {
            popup.setSize(width, height);
        }
    }

    /**
     * @return 当前显示的view
     * @author Mr.Huang
     * @date 2017-07-12
     * 获取当前Dialog的View
     */
    public View getContentView() {
        if (popup == null) {
            return null;
        }
        return popup.getContentView();
    }

    /**
     * 固定高度为屏幕的高
     *
     * @param fillScreen the fill screen
     */
    public void setFillScreen(boolean fillScreen) {
        isFillScreen = fillScreen;
    }

    /**
     * 固定高度为屏幕的一半
     *
     * @param halfScreen the half screen
     */
    public void setHalfScreen(boolean halfScreen) {
        isHalfScreen = halfScreen;
    }

    /**
     * Sets content view before.
     */
    protected void setContentViewBefore() {
    }

    /**
     * Sets content view after.
     *
     * @param contentView the content view
     */
    protected void setContentViewAfter(V contentView) {
    }

    /**
     * Sets animation.
     *
     * @param animRes the anim res
     */
    public void setAnimationStyle(@StyleRes int animRes) {
        if (popup != null) {
            popup.setAnimationStyle(animRes);
        }

    }

    /**
     * Sets on dismiss listener.
     *
     * @param onDismissListener the on dismiss listener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (popup != null) {
            popup.setOnDismissListener(onDismissListener);
        }

    }

    /**
     * Sets size.
     *
     * @param width  the width
     * @param height the height
     */
    public void setSize(int width, int height) {
        // 修复显示之前设置宽高无效问题
        this.width = width;
        this.height = height;
    }

    /**
     * Sets width.
     *
     * @param width the width
     * @see #setSize(int, int) #setSize(int, int)
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets height.
     *
     * @param height the height
     * @see #setSize(int, int) #setSize(int, int)
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Is showing boolean.
     *
     * @return the boolean
     */
    public boolean isShowing() {
        if (popup == null) {
            return false;
        }
        return popup.isShowing();
    }

    /**
     * Show.
     */
    @CallSuper
    public void show() {
        onShowPrepare();
        if (popup != null) {
            popup.show();
        }
    }

    /**
     * Dismiss.
     */
    public void dismiss() {
        if (popup != null) {
            popup.dismissAllowingStateLoss();
        }

    }

    /**
     * On key down boolean.
     *
     * @param keyCode the key code
     * @param event   the event
     * @return the boolean
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public final boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            return onKeyDown(keyCode, event);
        }
        return false;
    }

    /**
     * Gets window.
     *
     * @return the window
     */
    public Window getWindow() {
        if (popup == null) {
            return null;
        }
        return popup.getWindow();
    }

    /**
     * Gets root view.
     *
     * @return the root view
     */
    public ViewGroup getRootView() {
        if (popup != null) {
            return popup.getRootView();
        }
        return null;

    }

    /**
     * 设置弹框是否点击返回键消失
     *
     * @param dialogCancelable 是否可以点击返回键消失
     */
    public void setDialogCancelable(boolean dialogCancelable) {
        if (popup != null) {
            popup.setCancelable(dialogCancelable);
        }
    }

    /**
     * 设置弹框是否点击窗体外侧消失
     *
     * @param dialogCancelable 是否可以点击窗体外侧消失
     */
    public void setCancelableOutTouch(boolean dialogCancelable) {
        if (popup != null && popup.getDialog() != null) {
            popup.getDialog().setCanceledOnTouchOutside(dialogCancelable);
        }
    }
}
