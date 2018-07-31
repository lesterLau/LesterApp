package com.lesterlau.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.lesterlau.customview.R;


/**
 * 锁定宽度和高度的比例，默认宽高比为1:1
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class AspectRatioRelativeLayout extends RelativeLayout {

    private boolean lockWidth = true;
    private float widthWeight = 1;
    private float heightWeight = 1;

    public AspectRatioRelativeLayout(Context context) {
        this(context, null);
    }

    public AspectRatioRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatio, defStyle, 0);

        lockWidth = a.getBoolean(R.styleable.AspectRatio_lockWidth, true);
        widthWeight = a.getFloat(R.styleable.AspectRatio_widthWeight, 1);
        heightWeight = a.getFloat(R.styleable.AspectRatio_heightWeight, 1);

        widthWeight = widthWeight < 1 ? 1 : widthWeight;
        heightWeight = heightWeight < 1 ? 1 : heightWeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        if (lockWidth) {
            int lockSize = getMeasuredWidth();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (lockSize * heightWeight / widthWeight), MeasureSpec.EXACTLY);
        } else {
            int lockSize = getMeasuredHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (lockSize * widthWeight / heightWeight), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
