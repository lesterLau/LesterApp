package com.lesterlau.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;

public class AppManager {
    public static Application globalApplication;

    public static void init(Application application) {
        globalApplication = application;
        Utils.init(application);
        //ARouter配置
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
    }

    /**
     * 设置状态栏颜色
     * 字体颜色为白色
     *
     * @param statusColor      状态栏颜色
     * @param fitSystemWindows 根布局是否设置fitSystemWindows
     */
    public static void setStatusBarColor(Activity activity, int statusColor, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, fitSystemWindows);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    public static void setWhiteStatusBar(Activity activity, boolean fitSystemWindows) {
        setWhiteStatusBar(activity, fitSystemWindows, true);
    }

    /**
     * 设置状态栏为白色
     * 字体颜色为黑色
     *
     * @param isFullScreen     根布局是否充满屏幕
     * @param fitSystemWindows 根布局是否设置fitSystemWindows
     */
    public static void setWhiteStatusBar(Activity activity, boolean fitSystemWindows, boolean isFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isFullScreen) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.setStatusBarColor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Color.WHITE : Color.TRANSPARENT);
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, fitSystemWindows);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    public static void setLightAndTransStatusBar(Activity activity, boolean fitSystemWindows, boolean isFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isFullScreen) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, fitSystemWindows);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    /**
     * 设置状态栏背景为透明
     *
     * @param fitSystemWindows 根布局是否设置fitSystemWindows
     */
    public static void translucentStatusBar(Activity activity, boolean fitSystemWindows) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, fitSystemWindows);
                ViewCompat.requestApplyInsets(mChildView);
            }
        }
    }

    public static int getStatuBarSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//当api<19的时候，状态栏高度返回0，否则返回真实的statusBarHeight
            int statusBarsId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
            return Resources.getSystem().getDimensionPixelSize(statusBarsId);
        }
        return 0;
    }

    public static void setBarPadding(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {  //解决状态栏高度为warp_content或match_parent问题
                view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        lp.height = view.getHeight() + getStatuBarSize();
                        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatuBarSize(),
                                view.getPaddingRight(), view.getPaddingBottom());
                    }
                });
            } else {
                lp.height += getStatuBarSize();
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + getStatuBarSize(),
                        view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    public static void setBarMargin(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin,
                    layoutParams.topMargin + getStatuBarSize(),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }
    }
}
