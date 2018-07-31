package com.lesterlau.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.lesterlau.customview.R;

/**
 * 渐变色字体
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class ChangeColorTextView extends android.support.v7.widget.AppCompatTextView {

    private static final int DEFAULT_STARTCOLOR = 0xFF6F7CFF;
    private static final int DEFAULT_ENDCOLOR = 0xFFC76FFF;
    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();
    private int startColor;
    private int endColor;

    public ChangeColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorTextView, defStyleAttr, 0);
        startColor = a.getColor(R.styleable.ChangeColorTextView_startColor, DEFAULT_STARTCOLOR);
        endColor = a.getColor(R.styleable.ChangeColorTextView_endColor, DEFAULT_ENDCOLOR);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0, new int[]{startColor, endColor}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

}