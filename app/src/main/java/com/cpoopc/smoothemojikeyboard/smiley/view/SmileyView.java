package com.cpoopc.smoothemojikeyboard.smiley.view;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cpoopc.smoothemojikeyboard.R;
import com.cpoopc.smoothemojikeyboard.smiley.emoji.Emojicon;
import com.cpoopc.smoothemojikeyboard.smiley.emoji.EmojiconTextView;

/**
 * 显示单个表情view
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 18:31
 * Ver.: 0.1
 */
public class SmileyView extends FrameLayout{

    private EmojiconTextView emojiconTextView;
    private Emojicon mItem;

    public SmileyView(Context context) {
        super(context);
        init();
    }

    public SmileyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmileyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SmileyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.emojicon_item, this, true);
        emojiconTextView = (EmojiconTextView) layout.findViewById(R.id.emojicon_icon);
    }

    public void bindData(Emojicon item) {
        mItem = item;
        emojiconTextView.setText(item.getEmoji());

    }
}
