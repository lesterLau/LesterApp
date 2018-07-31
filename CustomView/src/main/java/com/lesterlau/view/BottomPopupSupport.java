package com.lesterlau.view;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.ViewGroup;


import com.lesterlau.customview.R;
import com.lesterlau.view.picker.common.popup.BottomPopup;


/**
 * 底部弹框的实现
 *
 * @Author lester
 * @Date 2018/7/30
 */
public abstract class BottomPopupSupport extends BottomPopup {

    public BottomPopupSupport(FragmentActivity activity) {
        super(activity);
        setAnimationStyle(R.style.AnimDownInDownOutOverShoot);
        if (getWindow() != null) {
            getWindow().setGravity(Gravity.BOTTOM);
        }
        setWidth(screenWidthPixels);//padding 布局文件中控制
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
