package com.cpoopc.smoothemotionkeyboard.inputboard;/**
 * Created by Administrator on 2015-09-01.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cpoopc.smoothemotionkeyboard.R;
import com.cpoopc.smoothemotionkeyboard.emotion.data.HahaEmotion;
import com.cpoopc.smoothemotionkeyboard.emotion.view.EmotionPager;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public class EditSoftInputLayout extends BaseSoftInputLayout implements View.OnClickListener {

    private View btnKeyBoard;

    // emotionView,otherView容器
    private View container;

    private View btnEmotion;
    private EmotionPager emotionView;

    private View btnOther;
    private View otherView;
    private View frame;
    private EditText editText;

    public EditSoftInputLayout(Context context) {
        super(context);
    }

    public EditSoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EditSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflateView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.edit_softinput_layout, this, true);
        container = layout.findViewById(R.id.container);
        frame = layout.findViewById(R.id.frame);
        editText = (EditText) layout.findViewById(R.id.edittext);
        setupKeyboardView(layout);
        setupEmotionView(layout);
        setupOtherView(layout);
    }

    private void setupKeyboardView(View layout) {
        btnKeyBoard = layout.findViewById(R.id.btnKeyBoard);
        btnKeyBoard.setOnClickListener(this);
    }

    private void setupOtherView(View layout) {
        btnOther = layout.findViewById(R.id.btnOther);
        btnOther.setOnClickListener(this);
        otherView = layout.findViewById(R.id.otherView);
        add2ShowViewList(otherView);
        add2MappingMap(btnOther, SHOW_OTHER, otherView);
    }

    private void setupEmotionView(View layout) {
        btnEmotion = layout.findViewById(R.id.btnEmotion);
        btnEmotion.setOnClickListener(this);
        emotionView = (EmotionPager) layout.findViewById(R.id.emotionPager);
        emotionView.bindData(HahaEmotion.DATA);
        add2ShowViewList(emotionView);
        add2MappingMap(btnEmotion, SHOW_EMOTION, emotionView);// btnEmotion-(SHOW_EMOTION-emotionView)
    }

    @Override
    protected View getContainer() {
        return container;
    }

    @Override
    protected View getFrame() {
        return frame;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    protected View getBtnKeyBoard() {
        return btnKeyBoard;
    }

}
