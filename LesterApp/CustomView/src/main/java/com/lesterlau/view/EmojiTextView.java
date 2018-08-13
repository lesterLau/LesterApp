package com.lesterlau.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.util.AttributeSet;

import com.lesterlau.customview.R;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;


/**
 * 表情
 *
 * @Author lester
 * @Date 2018/7/30
 */
public class EmojiTextView extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_EMOJI_SIZE = -1;

    private int emojiSize = -1;

    public EmojiTextView(Context context) {
        this(context, null);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmojiTextView);
        emojiSize = typedArray.getDimensionPixelSize(R.styleable.EmojiTextView_emojiSize, DEFAULT_EMOJI_SIZE);
        typedArray.recycle();
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmojiTextView, defStyle, 0);
        emojiSize = typedArray.getDimensionPixelSize(R.styleable.EmojiTextView_emojiSize, DEFAULT_EMOJI_SIZE);
        typedArray.recycle();
    }

    @Override
    public void setTextAppearance(Context context, int resid) {
        super.setTextAppearance(context, resid);
    }

    /**
     * 获取表情图片的大小。
     *
     * @return pixels
     */
    public int getEmojiSize() {
        return emojiSize;
    }

    /**
     * 设置表情图片的大小。
     *
     * @param emojiSize pixels。-1表示使用字体大小。
     */
    public void setEmojiSize(int emojiSize) {
        this.emojiSize = emojiSize;
    }

    /**
     * 设置表情图片的大小。
     *
     * @param emojiSize dp。-1表示使用字体大小。
     */
    public void setEmojiDPSize(int emojiSize) {
        if (emojiSize == -1) {
            this.emojiSize = emojiSize;
        } else {
            this.emojiSize = dp2px(emojiSize);
        }
    }
}
