package com.lesterlau.view.picker.picker;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;


import com.lesterlau.view.picker.widget.WheelView;

import java.util.ArrayList;

/**
 * 两级、三级联动选择器
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class LinkagePicker extends WheelPicker {

    protected ArrayList<String> firstList = new ArrayList<>();
    protected ArrayList<ArrayList<String>> secondList = new ArrayList<>();
    protected ArrayList<ArrayList<ArrayList<String>>> thirdList = new ArrayList<ArrayList<ArrayList<String>>>();
    protected OnLinkageListener onLinkageListener;
    protected String selectedFirstText = "", selectedSecondText = "", selectedThirdText = "";
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;
    protected boolean onlyTwo = false;

    public LinkagePicker(FragmentActivity activity) {
        super(activity);
    }

    /**
     * 二级联动选择器构造函数
     */
    public LinkagePicker(FragmentActivity activity, ArrayList<String> firstList,
                         ArrayList<ArrayList<String>> secondList) {
        this(activity, firstList, secondList, null);
    }

    /**
     * 三级联动选择器构造函数
     */
    public LinkagePicker(FragmentActivity activity, ArrayList<String> firstList,
                         ArrayList<ArrayList<String>> secondList,
                         ArrayList<ArrayList<ArrayList<String>>> thirdList) {
        super(activity);
        this.firstList = firstList;
        this.secondList = secondList;
        if (thirdList == null || thirdList.size() == 0) {
            this.onlyTwo = true;
        } else {
            this.thirdList = thirdList;
        }
    }

    public void setSelectedItem(String firstText, String secondText) {
        setSelectedItem(firstText, secondText, "");
    }

    public void setSelectedItem(String firstText, String secondText, String thirdText) {
        if (firstText == null || secondText == null || thirdText == null) return;

        for (int i = 0; i < firstList.size(); i++) {
            String ft = firstList.get(i);
            if (ft.contains(firstText)) {
                selectedFirstIndex = i;
                break;
            }
        }
        ArrayList<String> secondTexts = secondList.get(selectedFirstIndex);
        for (int j = 0; j < secondTexts.size(); j++) {
            String st = secondTexts.get(j);
            if (st.contains(secondText)) {
                selectedSecondIndex = j;
                break;
            }
        }
        if (TextUtils.isEmpty(thirdText) || thirdList.size() == 0) {
            return;//仅仅二级联动
        }
        ArrayList<String> thirdTexts = thirdList.get(selectedFirstIndex).get(selectedSecondIndex);
        for (int k = 0; k < thirdTexts.size(); k++) {
            String tt = thirdTexts.get(k);
            if (tt.contains(thirdText)) {
                selectedThirdIndex = k;
                break;
            }
        }
    }

    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        this.onLinkageListener = onLinkageListener;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        if (firstList.size() == 0 || secondList.size() == 0) {
            throw new IllegalArgumentException("please initial data at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        final WheelView firstView = new WheelView(activity);
        final int width = screenWidthPixels / 3;
        firstView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        firstView.setTextSize(textSize);
        firstView.setTextColor(textColorNormal, textColorFocus);
        firstView.setLineVisible(lineVisible);
        firstView.setLineColor(lineColor);
        firstView.setOffset(offset);
        layout.addView(firstView);
        final WheelView secondView = new WheelView(activity);
        secondView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        secondView.setTextSize(textSize);
        secondView.setTextColor(textColorNormal, textColorFocus);
        secondView.setLineVisible(lineVisible);
        secondView.setLineColor(lineColor);
        secondView.setOffset(offset);
        layout.addView(secondView);
        final WheelView thirdView = new WheelView(activity);
        thirdView.setLayoutParams(new LinearLayout.LayoutParams(width, WRAP_CONTENT));
        thirdView.setTextSize(textSize);
        thirdView.setTextColor(textColorNormal, textColorFocus);
        thirdView.setLineVisible(lineVisible);
        thirdView.setLineColor(lineColor);
        thirdView.setOffset(offset);
        layout.addView(thirdView);
        if (onlyTwo) {
            thirdView.setVisibility(View.GONE);
        }
        firstView.setItems(firstList, selectedFirstIndex);
        firstView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedFirstText = item;
                selectedFirstIndex = selectedIndex;
                selectedThirdIndex = 0;
                //根据第一级数据获取第二级数据
                secondView.setItems(secondList.get(selectedFirstIndex), isUserScroll ? 0 : selectedSecondIndex);
                if (thirdList.size() == 0) {
                    return;//仅仅二级联动
                }
                //根据第二级数据获取第三级数据
                thirdView.setItems(thirdList.get(selectedFirstIndex).get(0), isUserScroll ? 0 : selectedThirdIndex);
            }
        });
        secondView.setItems(secondList.get(selectedFirstIndex), selectedSecondIndex);
        secondView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedSecondText = item;
                selectedSecondIndex = selectedIndex;
                if (thirdList.size() == 0) {
                    return;//仅仅二级联动
                }
                //根据第二级数据获取第三级数据
                thirdView.setItems(thirdList.get(selectedFirstIndex).get(selectedSecondIndex), isUserScroll ? 0 : selectedThirdIndex);
            }
        });
        if (thirdList.size() == 0) {
            return layout;//仅仅二级联动
        }
        thirdView.setItems(thirdList.get(selectedFirstIndex).get(selectedSecondIndex), selectedThirdIndex);
        thirdView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedThirdText = item;
                selectedThirdIndex = selectedIndex;
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        super.onSubmit();
        if (onLinkageListener != null) {
            if (onlyTwo) {
                onLinkageListener.onPicked(selectedFirstText, selectedSecondText, null);
            } else {
                onLinkageListener.onPicked(selectedFirstText, selectedSecondText, selectedThirdText);
            }
        }
    }

    public interface OnLinkageListener {

        /**
         * on linked list picked
         *
         * @param first  first picker return
         * @param second second picker return
         * @param third  second picker return
         */
        void onPicked(String first, String second, String third);
    }
}
