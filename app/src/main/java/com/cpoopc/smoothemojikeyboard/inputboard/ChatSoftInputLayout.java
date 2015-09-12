package com.cpoopc.smoothemojikeyboard.inputboard;/**
 * Created by Administrator on 2015-09-01.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cpoopc.smoothemojikeyboard.R;
import com.cpoopc.smoothemojikeyboard.emotion.data.HahaEmotion;
import com.cpoopc.smoothemojikeyboard.emotion.view.EmotionPager;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public class ChatSoftInputLayout extends BaseSoftInputLayout implements View.OnClickListener {

    private View container;

    private View btnSmiley;
    private EmotionPager smileyView;

    private View btnOther;
    private View otherView;
    private View frame;
    private EditText editText;
    private Button btnSend;

    public ChatSoftInputLayout(Context context) {
        super(context);
    }

    public ChatSoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChatSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflateView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.chat_softinput_layout, this, true);
        container = layout.findViewById(R.id.container);
        frame = layout.findViewById(R.id.frame);
        editText = (EditText) layout.findViewById(R.id.edittext);
        btnSend = (Button) layout.findViewById(R.id.btnSend);
        setupSmileyView(layout);
        setupOtherView(layout);
    }

    private void setupOtherView(View layout) {
        btnOther = layout.findViewById(R.id.btnOther);
        btnOther.setOnClickListener(this);
        otherView = layout.findViewById(R.id.otherView);
        add2ShowViewList(otherView);
        add2MappingMap(btnOther, SHOW_OTHER, otherView);// btnSmiley-(SHOW_SMILE-smileyView)
    }

    private void setupSmileyView(View layout) {
        btnSmiley = layout.findViewById(R.id.btnSmile);
        btnSmiley.setOnClickListener(this);
        smileyView = (EmotionPager) layout.findViewById(R.id.smiley);
        smileyView.bindData(HahaEmotion.DATA);
        add2ShowViewList(smileyView);
        add2MappingMap(btnSmiley, SHOW_SMILE, smileyView);// btnSmiley-(SHOW_SMILE-smileyView)
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
        return null;
    }

    public void setOnSendClickListener(OnClickListener onSendClickListener) {
        btnSend.setOnClickListener(onSendClickListener);
    }

}
