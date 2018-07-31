package com.lester.lesterapp.test.view;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.lester.lesterapp.R;
import com.lesterlau.base.ui.activity.BaseActivity;
import com.lesterlau.view.ImageViewPlus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author lester
 * @Date 2018/7/30
 */
public class CircleImageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int TYPE_setBorderColor = 1;
    public static final int TYPE_setTextBackgroundColorRes = 2;
    public static final int TYPE_setTextColorRes = 3;
    @BindView(R.id.image1)
    ImageViewPlus image1;
    @BindView(R.id.rb1)
    RadioButton rb1;
    @BindView(R.id.rb2)
    RadioButton rb2;
    @BindView(R.id.rb3)
    RadioButton rb3;
    @BindView(R.id.radio)
    RadioGroup radio;
    @BindView(R.id.rb21)
    RadioButton rb21;
    @BindView(R.id.rb22)
    RadioButton rb22;
    @BindView(R.id.rb23)
    RadioButton rb23;
    @BindView(R.id.radio2)
    RadioGroup radio2;
    @BindView(R.id.cb_setUseDefaultStyle)
    CheckBox cbSetUseDefaultStyle;
    @BindView(R.id.cb_setIsFill)
    CheckBox cbSetIsFill;
    @BindView(R.id.setBorderWidth)
    SeekBar setBorderWidth;
    @BindView(R.id.setTextSize)
    SeekBar setTextSize;
    private int type = TYPE_setBorderColor;
    private int color = android.R.color.holo_red_light;

    @Override
    protected boolean isListenerNetwork() {
        return true;
    }

    @Override
    protected boolean isAttachTitle() {
        return true;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_circle_image_test;
    }

    @Override
    protected void initView() {
        image1.setText("AAA");
        radio.setOnCheckedChangeListener(this);
        radio2.setOnCheckedChangeListener(this);
        cbSetUseDefaultStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                image1.setUseDefaultStyle(isChecked);
            }
        });
        cbSetIsFill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                image1.setIsFill(isChecked);
            }
        });
        setBorderWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                image1.setBorderWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                image1.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                type = TYPE_setBorderColor;
                break;
            case R.id.rb2:
                type = TYPE_setTextBackgroundColorRes;
                break;
            case R.id.rb3:
                type = TYPE_setTextColorRes;
                break;
            case R.id.rb21:
                color = android.R.color.holo_red_light;
                break;
            case R.id.rb22:
                color = android.R.color.holo_orange_light;
                break;
            case R.id.rb23:
                color = android.R.color.holo_blue_light;
                break;
        }
        refresh();
    }

    private void refresh() {
        if (type == TYPE_setTextColorRes) {
            image1.setTextColorRes(color);
        } else if (type == TYPE_setTextBackgroundColorRes) {
            image1.setTextBackgroundColorRes(color);
        } else {
            image1.setBorderColor(color);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
