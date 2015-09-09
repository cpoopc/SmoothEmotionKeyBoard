package com.cpoopc.smoothemojikeyboard.emotion.view;/**
 * Created by cpoopc on 2015/9/9.
 */

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.cpoopc.smoothemojikeyboard.emotion.EmotionInputEventBus;
import com.cpoopc.smoothemojikeyboard.emotion.EmotionManager;
import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;
import com.cpoopc.smoothemojikeyboard.utils.DebugLog;

/**
 * User: cpoopc
 * Date: 2015-09-09
 * Time: 14:23
 * Ver.: 0.1
 */
public class EmotionEditText extends EditText implements TextWatcher,EmotionInputEventBus.EmotionInputEventListener {


    private String mDealingText;

    public EmotionEditText(Context context) {
        super(context);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addTextChangedListener(this);
        EmotionInputEventBus.instance.setEmotionInputEventListener(this);
    }

    @Override
    public void onEmotionInput(EmotionEntity emotionEntity) {
        DebugLog.e("emotion " + emotionEntity);
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        mDealingText = emotionEntity.getCode();
        EmotionManager.getImageSpan(emotionEntity);
        Editable text = getText();
        ImageSpan imageSpan = EmotionManager.getImageSpan(emotionEntity);
        if (selectionStart != selectionEnd) {
            DebugLog.e("----11");
            text.replace(selectionStart, selectionEnd, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            DebugLog.e("----12");
            text.insert(selectionStart, emotionEntity.getCode());
            text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mDealingText = null;
        DebugLog.e("----2");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        DebugLog.e("before:" + s);

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count >1 && !TextUtils.equals(s.subSequence(start, start + count), mDealingText)) {
//            DebugLog.e("复制的:" + s.subSequence(start, start + count));
            EmotionManager.parseCharSequence((SpannableStringBuilder) s, start, start + count);
        }
//        DebugLog.e("onTextChanged:" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
