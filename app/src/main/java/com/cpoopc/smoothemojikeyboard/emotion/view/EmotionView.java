package com.cpoopc.smoothemojikeyboard.emotion.view;/**
 * Created by cpoopc on 2015/9/1.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.cpoopc.smoothemojikeyboard.emotion.EmotionInputEventBus;
import com.cpoopc.smoothemojikeyboard.emotion.EmotionManager;
import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;

/**
 * 显示单个表情view
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 18:31
 * Ver.: 0.1
 */
public class EmotionView extends ImageView implements View.OnClickListener {

    private EmotionEntity mItem;

    public EmotionView(Context context) {
        super(context);
        init();
    }

    public EmotionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_INSIDE);
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View parent = (View) getParent();
        if (parent != null) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), parent.getHeight() == 0 ? 0 : parent.getHeight() / 3);
//            DebugLog.e("--"+(parent.getHeight() == 0 ? 0 : parent.getHeight() / 3));
        } else {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), getMeasuredHeight());
        }
    }

    public void bindData(EmotionEntity item) {
        mItem = item;
        setImageBitmap(EmotionManager.getBitmap(item.getSource()));
    }

    @Override
    public void onClick(View v) {
        EmotionInputEventBus.instance.postEmotion(mItem);
    }
}
