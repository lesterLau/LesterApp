package com.lesterlau.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;

/**
 * @author liubin
 * @date 2016/12/2
 */

public class CustomProgressDialog extends Dialog {
    private static CustomProgressDialog dialog;

    public CustomProgressDialog(@NonNull Context context) {
        this(context, 0);
    }

    public CustomProgressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private static CustomProgressDialog createDialog(Context context, final boolean canCancle) {
        dialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        dialog.setContentView(R.layout.progress_dialog_layout);
        dialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (canCancle) {
                        RxManager.getInstance().removeAll();
                        dialog.cancel();
                        return false;
                    } else {
                        return true;
                    }
                }
                return false;
            }
        });
        return dialog;
    }

    /**
     * 启动加载进度条
     */
    public static void showProgressDialog(Context context, boolean canCancle, boolean canceledOnTouchOutside) {
        try {
            if (dialog == null) {
                dialog = createDialog(context, canCancle);
                dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭加载进度条
     */
    public static void stopProgressDialog() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
