package com.cpoopc.smoothemojikeyboard.inputboard;/**
 * Created by Administrator on 2015-09-01.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpoopc.smoothemojikeyboard.R;
import com.cpoopc.smoothemojikeyboard.emotion.EmotionInputEventBus;
import com.cpoopc.smoothemojikeyboard.emotion.EmotionManager;
import com.cpoopc.smoothemojikeyboard.emotion.bean.EmotionEntity;
import com.cpoopc.smoothemojikeyboard.emotion.data.HahaEmotion;
import com.cpoopc.smoothemojikeyboard.emotion.view.EmotionPager;
import com.cpoopc.smoothemojikeyboard.utils.DebugLog;
import com.cpoopc.smoothemojikeyboard.utils.InputMethodUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public class SoftInputLayout extends LinearLayout implements View.OnClickListener, EmotionInputEventBus.EmotionInputEventListener {

    private View rootView;
    private double mVisibleHeight;
    private boolean mIsKeyboardShow;
    private View container;
    private View btnKeyBoard;
    public final static int SHOW_KEYBOARD = 0X1;
    public final static int SHOW_SMILE = 0X10;
    public final static int SHOW_OTHER = 0X11;
    private int showWhat;
    private int keyboardHeight = 400;
    private int rootViewHeight;
    private List<View> showViewList;
    private Map<View,ViewHolder> viewMapping;

    private View btnSmiley;
    private EmotionPager smileyView;

    private View btnOther;
    private View otherView;

    @Override
    public void onEmotionInput(EmotionEntity emotionEntity) {
        DebugLog.e("emotion " + emotionEntity);
        if (editText != null) {
            int selectionStart = editText.getSelectionStart();
            int selectionEnd = editText.getSelectionEnd();
            EmotionManager.getImageSpan(emotionEntity);
            Editable text = editText.getText();
            ImageSpan imageSpan = EmotionManager.getImageSpan(emotionEntity);
            if (selectionStart != selectionEnd) {
                text.replace(selectionStart, selectionEnd, emotionEntity.getCode());
                text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                text.replace(selectionStart,selectionEnd, )
            } else {
                text.insert(selectionStart, emotionEntity.getCode());
                text.setSpan(imageSpan, selectionStart, selectionStart + emotionEntity.getCode().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private static class ViewHolder{
        private int SHOW_TYPE;
        private View showView;

        public ViewHolder(int SHOW_TYPE, View showView) {
            this.SHOW_TYPE = SHOW_TYPE;
            this.showView = showView;
        }

        public int getSHOW_TYPE() {
            return SHOW_TYPE;
        }

        public View getShowView() {
            return showView;
        }
    }

    public SoftInputLayout(Context context) {
        super(context);
        init();
    }

    public SoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        EmotionInputEventBus.instance.setEmotionInputEventListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        updateLog();
        viewMapping = new HashMap<>();
        showViewList = new ArrayList<>();
        rootView = this;
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.softinput_layout, this, true);
        btnKeyBoard = layout.findViewById(R.id.btnKeyBoard);
        container = layout.findViewById(R.id.container);
        setupSmileyView(layout);
        setupOtherView(layout);
        // ....
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight();
                DebugLog.e("visiable height:" + mVisibleHeight + " mIsKeyboardShow:" + mIsKeyboardShow);
                updateLog();
                if (mIsKeyboardShow) {
                    showView(container);
                } else {
                    if (showWhat == 0) {
                        hideView(container);
                    } else if (showWhat == SHOW_KEYBOARD) {
                        showWhat = 0;
                    }
                }
            }
        });
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
        btnKeyBoard.setOnClickListener(this);
        smileyView = (EmotionPager) layout.findViewById(R.id.smiley);
        smileyView.bindData(HahaEmotion.DATA);
        add2ShowViewList(smileyView);
        add2MappingMap(btnSmiley, SHOW_SMILE, smileyView);// btnSmiley-(SHOW_SMILE-smileyView)
    }

    private void add2MappingMap(View view, int SHOW_TYPE, View showView) {
        viewMapping.put(view, new ViewHolder(SHOW_TYPE, showView));
    }

    private void add2ShowViewList(View view) {
        showViewList.add(view);
    }

    private void getKeyboardHeight() {
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.height();
        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }
        if (mVisibleHeight == visibleHeight) {
            return;
        }
        mVisibleHeight = visibleHeight;
        // Magic is here
        DebugLog.e("rootView.getHeight():" + rootView.getHeight() + "  rootViewHeight:" + rootViewHeight);
        if (mVisibleHeight < rootView.getHeight()) {
            int height = (int) (rootView.getHeight() - mVisibleHeight);
            if (keyboardHeight != height) {
                keyboardHeight = height;
                container.getLayoutParams().height = height;
                container.requestLayout();
            }
            mIsKeyboardShow = true;
            showWhat = SHOW_KEYBOARD;
        } else {
            mIsKeyboardShow = false;
            if (showWhat == SHOW_KEYBOARD) {
                showWhat = 0;
            }
        }
    }

    @Override
    public void onClick(View v) {
        updateLog();
        boolean isKeyBoardShowTemp = mIsKeyboardShow;
        if (v == btnKeyBoard) {
            // 点击键盘
            if (showWhat == SHOW_KEYBOARD) {
                hideView(container);
            } else if (showWhat == 0) {
                showView(container);
                showSoftInput();
            } else {
                hideAllViewExceptKeyBoard();
                showSoftInput();
            }
        }else {
            ViewHolder viewHolder = viewMapping.get(v);
            if (viewHolder != null) {
                int show_type = viewHolder.getSHOW_TYPE();
                View showView = viewHolder.getShowView();
                // 点击表情
                if (showWhat == show_type) {
                    // 隐藏表情,隐藏layout
                    hideView(showView);
                    hideView(container);
                } else {
                    hideAllViewExceptKeyBoard();
                    showView(showView);
                    showView(container);
                }
            }
        }

        if (isKeyBoardShowTemp) {
            hideSoftInput();
        }
        updateLog();
    }

    private void hideSoftInput() {
        DebugLog.e("隐藏软键盘");
        InputMethodUtils.hideSoftInput(getContext(), editText);
    }

    private void showSoftInput() {
        DebugLog.e("显示软键盘");
        showWhat = SHOW_KEYBOARD;
        InputMethodUtils.showSoftInputMethod(getContext(), editText);
    }

    private void hideView(View view) {
        view.setVisibility(GONE);
        if (view == container) {
            showWhat = 0;
        }
    }

    /**
     * 隐藏除了键盘外的view
     */
    private void hideAllViewExceptKeyBoard() {
        for (int i = 0; i < showViewList.size(); i++) {
            hideView(showViewList.get(i));
        }
    }

    private void showView(View view) {
        view.setVisibility(VISIBLE);
        if (view != container) {
            Iterator<Map.Entry<View, ViewHolder>> iterator = viewMapping.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<View, ViewHolder> next = iterator.next();
                if (next.getValue().getShowView() == view) {
                    showWhat = next.getValue().getSHOW_TYPE();
                    return;
                }
            }
        }
    }

    private TextView tvLog;

    public void setLogText(TextView tvLog) {
        this.tvLog = tvLog;
    }

    public void updateLog() {
        StringBuilder sb = new StringBuilder();
        if (showWhat == SHOW_KEYBOARD) {
            sb.append("SHOW_KEYBOARD ,");
        } else if (showWhat == SHOW_SMILE){
            sb.append("SHOW_SMILE ,");
        } else if (showWhat == SHOW_OTHER){
            sb.append("SHOW_OTHER ,");
        } else if (showWhat == 0) {
            sb.append("SHOW_NOTHING ,");
        }
        sb.append(" 软键盘-");
        sb.append(mIsKeyboardShow ? " 显示" : " 隐藏");
        DebugLog.d(sb.toString());
        if (tvLog != null) {
            tvLog.setText(sb.toString());
        }
    }

    private EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

}
